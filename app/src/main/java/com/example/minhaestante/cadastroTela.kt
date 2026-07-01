package com.example.minhaestante

//////////////////////////////// imports //////////////////////////////////////

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import com.example.minhaestante.ui.theme.AtkinsonHyperlegible
import com.example.minhaestante.ui.theme.Typography
import com.example.minhaestante.ui.theme.Baskervville
import android.R.id.bold

@Composable
fun cadastroTela(voltarLogin: () -> Unit) {

    /////////////////// VARIAVEIS //////////////////////////////////////

    //Dark Mode
    val darkTheme = isSystemInDarkTheme()

    //checar campo de email vazio
    var vazioEmail by remember { mutableStateOf(value = false) } //cria uma variável que o Compose observa e lembra entre redesenhos
    var vazioUsuario by remember { mutableStateOf(value = false) }
    var erroSenha by remember { mutableStateOf<String?>(null) }

    //variaveis ON CHANGE
    var usuario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var vizivelSenha by remember { mutableStateOf(false) }

    ///////////////////////// ESTRUTURA ////////////////////////////////////////////

    //Column para por as informações uma abaixo da outra
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        //ESPAÇO
        Spacer(modifier = Modifier.height(38.dp))

        /////////////////////////// LOGO ///////////////////////////////////////
        Image(
            //painterResource() carrega uma imagem da pasta res/drawable do projeto
            painter = painterResource(id = if (darkTheme) R.drawable.logindark else R.drawable.logo),
            contentDescription = "Imagem da logo do Minha Estante",
            modifier = Modifier.size(165.dp) //tamanho da imagem
        )

        //ESPAÇO
        Spacer(modifier = Modifier.height(8.dp))

        ////////////////////////// CARD CENTRAL //////////////////////////////////
        Card(

            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (darkTheme) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.background
            ),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.surface)

        ) {
            //Espaçamento dos elementos dentro do card
            Column(
                modifier = Modifier.padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 32.dp,
                    bottom = 32.dp
                )
            ) {

                /////////////////////// USUARIO /////////////////////////////////////

                //TEXT
                Text(
                    text = "Nome de Usuário",
                    fontFamily = Baskervville,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                //ESPAÇO
                Spacer(modifier = Modifier.height(height = 8.dp))

                //CAIXA DE INSERÇÃO
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("campoUsuario"), //TESTE
                    value = usuario,
                    onValueChange = {
                        usuario = it
                        vazioUsuario = false
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        focusedLabelColor = MaterialTheme.colorScheme.secondary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.secondary
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Ícone de usuário",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    // Texto Default
                    label = {
                        Text(
                            "Insira seu nome de usuário",
                            fontFamily = AtkinsonHyperlegible,
                            fontSize = 14.sp
                        )
                    },
                    // ERROR FALTA DE EMAIL
                    supportingText = { if (vazioUsuario) Text("Campo Obrigatório!") },
                    isError = vazioUsuario
                )

                //ESPAÇO
                Spacer(modifier = Modifier.height(24.dp))

                //////////////////////////// EMAIL ///////////////////////////////////////

                // TEXT
                Text(
                    text = "Email",
                    fontFamily = Baskervville,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                //ESPAÇO
                Spacer(modifier = Modifier.height(height = 8.dp))

                //CAIXA DE INSERÇÃO
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("campoEmail"),
                    value = email,
                    onValueChange = {
                        email = it
                        vazioEmail = false
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        focusedLabelColor = MaterialTheme.colorScheme.secondary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.secondary
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Ícone de usuário",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    // Texto Default
                    label = {
                        Text(
                            "Insira um email válido",
                            fontFamily = AtkinsonHyperlegible,
                            fontSize = 14.sp
                        )
                    },

                    // ERROR FALTA DE EMAIL
                    supportingText = { if (vazioEmail) Text("Campo Obrigatório!") },
                    isError = vazioEmail
                )

                //ESPAÇO
                Spacer(modifier = Modifier.height(24.dp))

                ///////////////////////////// SENHA ////////////////////////////////////////

                //TEXTO
                Text(
                    text = "Senha",
                    fontFamily = Baskervville,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                //ESPAÇO
                Spacer(modifier = Modifier.height(height = 8.dp))

                //CAIXA DE INSERÇÃO
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("campoSenha"),
                    value = senha,
                    onValueChange = {
                        senha = it
                        erroSenha = null
                                    },
                    visualTransformation = if (vizivelSenha) VisualTransformation.None else PasswordVisualTransformation(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        focusedLabelColor = MaterialTheme.colorScheme.secondary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.secondary
                    ),
                    // Texto Default
                    label = {
                        Text(
                            "Escolha uma senha",
                            fontFamily = AtkinsonHyperlegible,
                            fontSize = 14.sp
                        )
                    },
                    supportingText = {erroSenha?.let { Text(it) }},
                    isError = erroSenha != null,

                    //icone do olho
                    trailingIcon = {
                        IconButton(onClick = { vizivelSenha = !vizivelSenha }) {
                            Icon(
                                imageVector = if (vizivelSenha) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Mostrar senha",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                )

                // ESPAÇO
                Spacer(modifier = Modifier.height(24.dp))

                ///////////////////////////// BOTAO CADASTRAR ///////////////////////////////
                Button(
                    onClick = {
                        //Confere se usuário colocou o email
                        if (email.isEmpty()) {
                            vazioEmail = true
                        } else {
                            vazioEmail = false
                        }
                        //Confere se usuário colocou o user
                        if (usuario.isEmpty()) {
                            vazioUsuario = true
                        } else {
                            vazioUsuario = false
                        }
                        //Confere se o usuário colocou a senha
                        erroSenha = when{
                            senha.isEmpty() -> "Campo obrigatório"
                            senha.length < 8 -> "Mínimo de 6 caracteres"
                            else -> null
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Text(
                        "Cadastrar",
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

@Preview(showBackground = true)
@Composable
private fun cadastroTelaPreview() {
    cadastroTela {}
}