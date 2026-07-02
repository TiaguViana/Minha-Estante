package com.example.minhaestante.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.minhaestante.ui.screens.*
import androidx.compose.runtime.key

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Cadastro : Screen("cadastro")
    object Main : Screen("main_screen")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // O app agora começa na tela de Splash!
    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        // 1. Tela de Splash
        composable(Screen.Splash.route) {
            SplashScreen(onTimeout = {
                // Após os 3 segundos da Splash, vai para o Login de forma limpa
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }

        // 2. Tela de Login
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    // Ao logar, vai para a MainScreen e limpa o Login do histórico
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToCadastro = {
                    // Quando clicar em "Crie Agora!", navega para a tela de Cadastro
                    navController.navigate(Screen.Cadastro.route)
                }
            )
        }

        // 3. Tela de Cadastro (CORRIGIDO ADICIONANDO O RETORNO)
        composable(Screen.Cadastro.route) {
            CadastroScreen(
                onCadastroSuccess = {
                    // Após se cadastrar com sucesso, volta para a tela de login
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Cadastro.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    // Executado quando clicar na seta de voltar da barra superior do Cadastro
                    navController.popBackStack()
                }
            )
        }

        // 4. Tela Principal (Dashboard Firebase CRUD)
        composable(Screen.Main.route) {
            val currentUid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""

            // O 'key' força a MainScreen a resetar o estado interno caso o UID mude (logout/login)
            key(currentUid) {
                MainScreen()
            }
        }
    }
}