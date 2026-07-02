package com.example.minhaestante.ui.screens

//////////////////////////////// imports //////////////////////////////////////
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minhaestante.R
import com.example.minhaestante.data.repository.FirebaseRepo
import com.example.minhaestante.ui.theme.AtkinsonHyperlegible
import com.example.minhaestante.ui.theme.Baskervville

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToCadastro: () -> Unit
) {
    val context = LocalContext.current
    val firebaseRepo = remember { FirebaseRepo() } // Instanciando o repositório
    val darkTheme = isSystemInDarkTheme()

    // Estados de validação local
    var vazioEmail by remember { mutableStateOf(false) }
    var erroSenha by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) } // Estado de carregamento

    // Estados dos campos de entrada
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var senhaVizivel by remember { mutableStateOf(false) }

    val textoErroObrigatorio = stringResource(id = R.string.erro_obrigatorio)
    val textoErroSenhaCurta = stringResource(id = R.string.erro_senha_curta)
    val textoSucessoLogin = stringResource(id = R.string.sucesso_login)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Image(
            painter = painterResource(id = if (darkTheme) R.drawable.logindark else R.drawable.logo),
            contentDescription = stringResource(id = R.string.desc_logo_app),
            modifier = Modifier.size(165.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary)
        ) {
            Column(modifier = Modifier.padding(24.dp, 32.dp)) {

                // EMAIL
                Text(
                    text = stringResource(id = R.string.label_email),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = Baskervville,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().testTag("campoEmail"),
                    value = email,
                    onValueChange = { email = it; vazioEmail = false },
                    shape = RoundedCornerShape(16.dp),
                    trailingIcon = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = stringResource(id = R.string.desc_icone_email),
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.outline // Ícone com a cor outline
                        )
                    },
                    label = { Text(stringResource(id = R.string.hint_email)) },
                    supportingText = { if (vazioEmail) Text(stringResource(id = R.string.erro_obrigatorio)) },
                    isError = vazioEmail,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.outline,
                        unfocusedTextColor = MaterialTheme.colorScheme.outline,
                        focusedBorderColor = MaterialTheme.colorScheme.outline,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.outline,
                        unfocusedLabelColor = MaterialTheme.colorScheme.outline,
                        cursorColor = MaterialTheme.colorScheme.outline
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // SENHA
                Text(
                    text = stringResource(id = R.string.label_senha),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontFamily = Baskervville,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().testTag("campoSenha"),
                    value = senha,
                    onValueChange = { senha = it; erroSenha = null },
                    visualTransformation = if (senhaVizivel) VisualTransformation.None else PasswordVisualTransformation(),
                    shape = RoundedCornerShape(16.dp),
                    label = { Text(stringResource(id = R.string.hint_senha)) },
                    supportingText = { erroSenha?.let { Text(it) } },
                    isError = erroSenha != null,
                    trailingIcon = {
                        IconButton(onClick = { senhaVizivel = !senhaVizivel }) {
                            Icon(
                                imageVector = if (senhaVizivel) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = stringResource(
                                    id = if (senhaVizivel) R.string.desc_ocultar_senha else R.string.desc_mostrar_senha
                                ),
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.outline // Ícone do olho com a cor outline
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.outline,
                        unfocusedTextColor = MaterialTheme.colorScheme.outline,
                        focusedBorderColor = MaterialTheme.colorScheme.outline,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.outline,
                        unfocusedLabelColor = MaterialTheme.colorScheme.outline,
                        cursorColor = MaterialTheme.colorScheme.outline
                    )
                )

                // Redefinir senha
                Text(
                    text = stringResource(id = R.string.link_esqueceu_senha),
                    fontFamily = AtkinsonHyperlegible,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable(onClickLabel = stringResource(id = R.string.desc_link_esqueceu_senha)) { /* Esqueci senha */ }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // BOTÃO ENTRAR
                Button(
                    onClick = {
                        vazioEmail = email.isEmpty()

                        erroSenha = when {
                            senha.isEmpty() -> textoErroObrigatorio
                            senha.length < 6 -> textoErroSenhaCurta
                            else -> null
                        }

                        if (!vazioEmail && erroSenha == null) {
                            isLoading = true

                            firebaseRepo.loginUsuario(
                                email = email,
                                senha = senha,
                                onSuccess = {
                                    isLoading = false
                                    Toast.makeText(context, textoSucessoLogin, Toast.LENGTH_SHORT).show()
                                    onLoginSuccess()
                                },
                                onFailure = { erro ->
                                    isLoading = false
                                    Toast.makeText(context, erro, Toast.LENGTH_LONG).show()
                                }
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onBackground)
                    } else {
                        Text(
                            text = stringResource(id = R.string.btn_entrar),
                            color = if (darkTheme) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurface,
                            fontSize = 18.sp,
                            fontFamily = Baskervville,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // CADASTRO
        Text(
            text = stringResource(id = R.string.link_criar_conta),
            fontFamily = AtkinsonHyperlegible,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(top = 24.dp)
                .clickable(onClickLabel = stringResource(id = R.string.desc_link_criar_conta)) { onNavigateToCadastro() }
        )
    }
}