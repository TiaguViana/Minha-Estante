package com.example.minhaestante.utils

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

object BiometricHelper {

    private fun Context.findActivity(): FragmentActivity? {
        var currentContext = this
        while (currentContext is ContextWrapper) {
            if (currentContext is FragmentActivity) {
                return currentContext
            }
            currentContext = currentContext.baseContext
        }
        return null
    }

    /**
     * BIOMETRIC_WEAK | DEVICE_CREDENTIAL só é uma combinação válida a partir da API 30.
     * Em versões anteriores, authenticate() lança IllegalArgumentException nessa combinação,
     * o que fazia o fluxo cair silenciosamente no onErro(). Aqui escolhemos os
     * authenticators certos pra versão do aparelho, e checamos canAuthenticate()
     * antes de sequer tentar mostrar o prompt.
     */
    private fun authenticatorsSuportados(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            BIOMETRIC_WEAK or DEVICE_CREDENTIAL
        } else {
            // Abaixo da API 30 não dá pra combinar WEAK com DEVICE_CREDENTIAL.
            // Preferimos biometria; se o aparelho não tiver sensor, caímos pra credencial do dispositivo.
            BIOMETRIC_WEAK
        }
    }

    fun autenticar(context: Context, onSucesso: () -> Unit, onErro: () -> Unit) {
        val activity = context.findActivity() ?: run {
            onErro()
            return
        }
        val executor = ContextCompat.getMainExecutor(activity)
        val biometricManager = BiometricManager.from(activity)

        var authenticators = authenticatorsSuportados()
        var disponibilidade = biometricManager.canAuthenticate(authenticators)

        // Se biometria não está disponível/cadastrada em API < 30, tenta fallback só com credencial do dispositivo
        if (disponibilidade != BiometricManager.BIOMETRIC_SUCCESS &&
            Build.VERSION.SDK_INT < Build.VERSION_CODES.R
        ) {
            authenticators = DEVICE_CREDENTIAL
            disponibilidade = biometricManager.canAuthenticate(authenticators)
        }

        if (disponibilidade != BiometricManager.BIOMETRIC_SUCCESS) {
            // Nenhum método de autenticação disponível nesse aparelho (sem PIN/senha/digital configurados etc.)
            onErro()
            return
        }

        val biometricPrompt = BiometricPrompt(
            activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    activity.runOnUiThread { onSucesso() }
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    activity.runOnUiThread { onErro() }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // Chamado em tentativas erradas, mantemos ativo sem fechar o fluxo
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Estante Secreta")
            .setSubtitle("Autentique-se para ver seus livros ocultos")
            .setAllowedAuthenticators(authenticators)
            .build()

        try {
            biometricPrompt.authenticate(promptInfo)
        } catch (e: Exception) {
            e.printStackTrace()
            onErro()
        }
    }
}