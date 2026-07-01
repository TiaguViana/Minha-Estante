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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.platform.testTag
import com.example.minhaestante.ui.theme.AtkinsonHyperlegible
import com.example.minhaestante.ui.theme.Typography
import com.example.minhaestante.ui.theme.Baskervville
import android.R.id.bold

@Composable
fun loginTela(irParaCadastro: () -> Unit) {

    /////////////////// VARIAVEIS //////////////////////////////////////

    //variavel DARK MODE
    val darkTheme = isSystemInDarkTheme()

    // variavel campo de email vazio
    var vazioEmail by remember { mutableStateOf(value = false) }
    var erroSenha by remember { mutableStateOf<String?>(null) }

    //variavel ON Change
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var senhaVizivel by remember { mutableStateOf(false) }

    /////////////////////// ESTRUTURA ////////////////////////////////////

    //informações uma abaixo da outra
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally, // alinhamento x
        verticalArrangement = Arrangement.Top // alinhamento y
    ) {
        // ESPAÇO
        Spacer(modifier = Modifier.height(48.dp))

        ////////////////// LOGO //////////////////////////////////////////////////
        Image(
            painter = painterResource(
                id = if (darkTheme) R.drawable.logindark else R.drawable.logo
            ),
            contentDescription = "Logo Minha Estante",
            modifier = Modifier.size(165.dp)
        )

        // ESPAÇO
        Spacer(modifier = Modifier.height(24.dp))

        /////////////////////////////// Card central /////////////////////////////
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (darkTheme)
                    MaterialTheme.colorScheme.onBackground
                else
                    MaterialTheme.colorScheme.background
            ),

            //Borda
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.surface)
        ) {
            //Espaçamento dos elementos dentro do card
            Column(
                modifier = Modifier.padding(
                    start = 24.dp,   // esquerda
                    end = 24.dp,     // direita
                    top = 32.dp,
                    bottom = 32.dp
                )
            ) {

                ////////////////// EMAIL //////////////////////////////////////

                //Texto
                Text(
                    text = "Email",
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = Baskervville,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp

                )

                //espaço para campo de inserção
                Spacer(modifier = Modifier.height(height = 8.dp))

                //campo de inserção Email
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
                        focusedBorderColor = MaterialTheme.colorScheme.onSecondary,

                        unfocusedLabelColor = if (darkTheme)
                            MaterialTheme.colorScheme.secondary
                        else
                            MaterialTheme.colorScheme.onSecondary,
                        focusedLabelColor = MaterialTheme.colorScheme.secondary
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Ícone de usuário",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    label = {
                        Text(
                            "Insira seu email",
                            fontFamily = AtkinsonHyperlegible,
                            fontSize = 14.sp
                        )
                    },

                    //ERROR EMAIL
                    supportingText = { if (vazioEmail) Text("Campo Obrigatório!") },
                    isError = vazioEmail

                )

                Spacer(modifier = Modifier.height(24.dp))

                ////////////////////// SENHA //////////////////////////////////////
                Text(
                    text = "Senha",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    fontFamily = Baskervville,
                    fontWeight = FontWeight.Bold,
                    //mudar o style da tipografia
                )

                //espaço para campo de inserção
                Spacer(modifier = Modifier.height(height = 8.dp))

                //campo de inserção SENHA
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("campoSenha"),
                    value = senha,
                    onValueChange = {
                        senha = it
                        erroSenha = null
                    },
                    visualTransformation = if (senhaVizivel)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                        focusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                        unfocusedLabelColor = if (darkTheme)
                            MaterialTheme.colorScheme.secondary
                        else
                            MaterialTheme.colorScheme.onSecondary,
                        focusedLabelColor = MaterialTheme.colorScheme.secondary
                    ),
                    label = {
                        Text(
                            "Insira sua senha",
                            fontFamily = AtkinsonHyperlegible,
                            fontSize = 14.sp
                        )
                    },

                    supportingText = { erroSenha?.let { Text(it) } },
                    isError = erroSenha != null,

                    //icone do olho
                    trailingIcon = {
                        IconButton(onClick = { senhaVizivel = !senhaVizivel }) {
                            Icon(
                                imageVector = if (senhaVizivel)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,
                                contentDescription = "Mostrar senha",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                )
                ////////////////  Redefinir senha //////////////////

                ///// MUDAR DE TEXT PARA BUTTON ////////////
                Text(
                    text = "Esqueceu a senha ?",
                    fontFamily = AtkinsonHyperlegible,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                ////////////// BOTÃO ENTRAR /////////////////
                Button(
                    onClick = {
                        if (email.isEmpty()) {
                            vazioEmail = true
                        } else {
                            vazioEmail = false
                        }
                        erroSenha = when {
                            senha.isEmpty() -> "Campo obrigatório!"
                            senha.length < 8 -> "Mínimo de 8 caracteres"
                            else -> null
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        "Entrar",
                        color = if (darkTheme) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.primary,
                        fontSize = 18.sp,
                        fontFamily = Baskervville,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

        }

        ////////////// CADASTRO //////////////////
        ///// MUDAR DE TEXT PARA BUTTON ////////////
        Text(
            text = "Não tem uma conta? Crie Agora!",
            fontFamily = AtkinsonHyperlegible,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(top = 24.dp)
                .clickable { irParaCadastro() }
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun loginTelaPreview() {
    loginTela(
        irParaCadastro = {}
    )
}