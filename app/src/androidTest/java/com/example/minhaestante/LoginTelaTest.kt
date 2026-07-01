package com.example.minhaestante
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.minhaestante.ui.theme.MinhaEstanteTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
@RunWith(AndroidJUnit4::class)

class LoginTelaTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            MinhaEstanteTheme {
                loginTela(irParaCadastro = {})
            }
        }
    }

    @Test
    fun camposVazios_exibeMensagensDeErro() {
        composeTestRule.onNodeWithText("Entrar").performClick()

        composeTestRule.onAllNodesWithText("Campo obrigatório!").assertCountEquals(2)
    }

    @Test
    fun senhasCurta_exibeErroDeMinimo() {
        composeTestRule.onNodeWithTag("campoSenha").performTextInput("123")
        composeTestRule.onNodeWithText("Entrar").performClick()

        composeTestRule.onNodeWithText("Mínimo de 8 caracteres").assertIsDisplayed()
    }

    @Test
    fun apenasEmailVazio_senhaOk() {
        composeTestRule.onNodeWithTag("campoSenha").performTextInput("senha123")
        composeTestRule.onNodeWithText("Entrar").performClick()

        composeTestRule.onNodeWithText("Campo obrigatório!").assertIsDisplayed() // só o do email
        composeTestRule.onNodeWithText("Mínimo de 8 caracteres").assertDoesNotExist()
    }

    @Test
    fun tudoPreenchidoCorretamente_semErros() {
        composeTestRule.onNodeWithTag("campoEmail").performTextInput("sabrina@email.com")
        composeTestRule.onNodeWithTag("campoSenha").performTextInput("senha123")
        composeTestRule.onNodeWithText("Entrar").performClick()

        composeTestRule.onNodeWithText("Campo obrigatório!").assertDoesNotExist()
        composeTestRule.onNodeWithText("Mínimo de 8 caracteres").assertDoesNotExist()
    }
}