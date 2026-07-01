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
            contentDescription = "Logo",
            modifier = Modifier.size(165.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (darkTheme) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.background
            ),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(24.dp, 32.dp)) {

                // EMAIL
                Text(
                    text = "E-mail",
                    color = MaterialTheme.colorScheme.primary,
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
                        Icon(Icons.Default.Email, "Email", modifier = Modifier.size(16.dp))
                    },
                    label = { Text("Digite seu e-mail") },
                    supportingText = { if (vazioEmail) Text("Campo obrigatório") },
                    isError = vazioEmail
                )

                Spacer(modifier = Modifier.height(24.dp))

                // SENHA
                Text(
                    text = "Senha",
                    color = MaterialTheme.colorScheme.primary,
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
                    label = { Text("Digite sua senha") },
                    supportingText = { erroSenha?.let { Text(it) } },
                    isError = erroSenha != null,
                    trailingIcon = {
                        IconButton(onClick = { senhaVizivel = !senhaVizivel }) {
                            Icon(
                                imageVector = if (senhaVizivel) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Mostrar/Ocultar",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                )

                // Redefinir senha
                Text(
                    text = "Esqueceu sua senha?",
                    fontFamily = AtkinsonHyperlegible,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp).clickable { /* Esqueci senha */ }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // BOTÃO ENTRAR
                Button(
                    onClick = {
                        vazioEmail = email.isEmpty()

                        erroSenha = when {
                            senha.isEmpty() -> "Campo obrigatório"
                            senha.length < 6 -> "A senha deve ter pelo menos 6 caracteres"
                            else -> null
                        }

                        if (!vazioEmail && erroSenha == null) {
                            isLoading = true // Inicia o carregamento

                            // Usando a função do FirebaseRepo
                            firebaseRepo.loginUsuario(
                                email = email,
                                senha = senha,
                                onSuccess = {
                                    isLoading = false
                                    Toast.makeText(context, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                                    onLoginSuccess() // Vai para a MainScreen
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
                    enabled = !isLoading, // Desativa se estiver carregando
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.primary)
                    } else {
                        Text(
                            text = "Entrar",
                            color = if (darkTheme) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.primary,
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
            text = "Não tem uma conta? Cadastre-se",
            fontFamily = AtkinsonHyperlegible,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 24.dp).clickable { onNavigateToCadastro() }
        )
    }
}