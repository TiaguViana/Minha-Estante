package com.example.minhaestante

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.minhaestante.ui.theme.MinhaEstanteTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MinhaEstanteTheme {

                var telaAtual by remember {
                    mutableStateOf("splash")
                }

                when (telaAtual) {

                    "splash" -> splashScreen(
                        onTimeout = {
                            telaAtual = "login"
                        }
                    )

                    "login" -> loginTela(
                        irParaCadastro = {
                            telaAtual = "cadastro"
                        }
                    )

                    "cadastro" -> cadastroTela(
                        voltarLogin = {
                            telaAtual = "login"
                        }
                    )
                }
            }
        }
    }
}


