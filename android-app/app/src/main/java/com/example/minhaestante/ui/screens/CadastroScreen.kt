package com.example.minhaestante.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.minhaestante.ui.theme.Baskervville

@Composable
fun CadastroScreen(
    onCadastroSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val firebaseRepo = remember { FirebaseRepo() } // Instanciando o repositório

    // Variáveis de estado
    val darkTheme = isSystemInDarkTheme()
    var vazioEmail by remember { mutableStateOf(false) }
    var vazioUsuario by remember { mutableStateOf(false) }
    var erroSenha by remember { mutableStateOf<String?>(null) }
    var usuario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var vizivelSenha by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // BOTÃO VOLTAR
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.desc_botao_voltar),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = if (darkTheme) R.drawable.logindark else R.drawable.logo),
            contentDescription = stringResource(id = R.string.desc_logo_app),
            modifier = Modifier.size(165.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(24.dp, 32.dp)) {

                // USUÁRIO
                Text(
                    text = stringResource(id = R.string.label_usuario),
                    fontFamily = Baskervville,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it; vazioUsuario = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("campoUsuario"),
                    shape = RoundedCornerShape(16.dp),
                    trailingIcon = { Icon(Icons.Default.Person, null, modifier = Modifier.size(16.dp)) },
                    label = { Text(stringResource(id = R.string.hint_usuario)) },
                    supportingText = { if (vazioUsuario) Text(stringResource(id = R.string.erro_obrigatorio)) },
                    isError = vazioUsuario,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                        cursorColor = MaterialTheme.colorScheme.onBackground
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // EMAIL
                Text(
                    text = stringResource(id = R.string.label_email),
                    fontFamily = Baskervville,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; vazioEmail = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("campoEmail"),
                    shape = RoundedCornerShape(16.dp),
                    trailingIcon = { Icon(Icons.Default.Email, null, modifier = Modifier.size(16.dp)) },
                    label = { Text(stringResource(id = R.string.hint_email_cadastro)) },
                    supportingText = { if (vazioEmail) Text(stringResource(id = R.string.erro_obrigatorio)) },
                    isError = vazioEmail,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                        cursorColor = MaterialTheme.colorScheme.onBackground
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // SENHA
                Text(
                    text = stringResource(id = R.string.label_senha),
                    fontFamily = Baskervville,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = senha,
                    onValueChange = { senha = it; erroSenha = null },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("campoSenha"),
                    visualTransformation = if (vizivelSenha) VisualTransformation.None else PasswordVisualTransformation(),
                    shape = RoundedCornerShape(16.dp),
                    label = { Text(stringResource(id = R.string.hint_escolha_senha)) },
                    supportingText = { erroSenha?.let { Text(it) } },
                    isError = erroSenha != null,
                    trailingIcon = {
                        IconButton(onClick = { vizivelSenha = !vizivelSenha }) {
                            Icon(
                                imageVector = if (vizivelSenha) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = stringResource(
                                    id = if (vizivelSenha) R.string.desc_ocultar_senha else R.string.desc_mostrar_senha
                                ),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                        cursorColor = MaterialTheme.colorScheme.onBackground
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // BOTÃO CADASTRAR
                Button(
                    onClick = {
                        vazioEmail = email.isEmpty()
                        vazioUsuario = usuario.isEmpty()
                        erroSenha = when {
                            senha.isEmpty() -> "Campo obrigatório"
                            senha.length < 6 -> "A senha deve conter no mínimo 6 caracteres"
                            else -> null
                        }

                        if (!vazioEmail && !vazioUsuario && erroSenha == null) {
                            isLoading = true
                            firebaseRepo.cadastrarUsuario(
                                nome = usuario,
                                email = email,
                                senha = senha,
                                onSuccess = {
                                    isLoading = false
                                    Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                                    onCadastroSuccess()
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
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.primary)
                    } else {
                        Text(
                            text = "Cadastrar",
                            fontFamily = Baskervville,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (darkTheme) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}