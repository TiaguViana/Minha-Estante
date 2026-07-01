package com.example.minhaestante.ui.screens

//////////////////////////////// imports //////////////////////////////////////
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minhaestante.R
import com.example.minhaestante.data.User
import com.example.minhaestante.ui.theme.Baskervville
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun CadastroScreen(
    onCadastroSuccess: () -> Unit
) {
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }

    // Variáveis de estado
    val darkTheme = isSystemInDarkTheme()
    var vazioEmail by remember { mutableStateOf(false) }
    var vazioUsuario by remember { mutableStateOf(false) }
    var erroSenha by remember { mutableStateOf<String?>(null) }
    var usuario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var vizivelSenha by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(38.dp))

        Image(
            painter = painterResource(id = if (darkTheme) R.drawable.logindark else R.drawable.logo),
            contentDescription = "Logo do Aplicativo",
            modifier = Modifier.size(165.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (darkTheme) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.background
            ),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(24.dp, 32.dp)) {

                // USUÁRIO
                Text(
                    text = "Usuário",
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
                    label = { Text("Digite seu nome de usuário") },
                    supportingText = { if (vazioUsuario) Text("Campo obrigatório") },
                    isError = vazioUsuario
                )

                Spacer(modifier = Modifier.height(24.dp))

                // EMAIL
                Text(
                    text = "E-mail",
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
                    label = { Text("Digite seu e-mail") },
                    supportingText = { if (vazioEmail) Text("Campo obrigatório") },
                    isError = vazioEmail
                )

                Spacer(modifier = Modifier.height(24.dp))

                // SENHA
                Text(
                    text = "Senha",
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
                    label = { Text("Escolha uma senha") },
                    supportingText = { erroSenha?.let { Text(it) } },
                    isError = erroSenha != null,
                    trailingIcon = {
                        IconButton(onClick = { vizivelSenha = !vizivelSenha }) {
                            Icon(
                                imageVector = if (vizivelSenha) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Mostrar ou ocultar senha",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // BOTAO CADASTRAR
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
                            auth.createUserWithEmailAndPassword(email, senha)
                                .addOnSuccessListener { authResult ->
                                    val uid = authResult.user?.uid
                                    if (uid != null) {
                                        val novoUsuario = User(
                                            nome = usuario,
                                            email = email,
                                            status = "ativo",
                                            cadastro = Timestamp.now(),
                                            isAdmin = false
                                        )
                                        firestore.collection("usuarios").document(uid).set(novoUsuario)
                                            .addOnSuccessListener {
                                                Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                                                onCadastroSuccess()
                                            }
                                            .addOnFailureListener { e ->
                                                Toast.makeText(context, "Erro ao salvar perfil: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                                            }
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Erro na autenticação: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                                }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
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