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

    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        // 1. Tela de Splash
        composable(Screen.Splash.route) {
            SplashScreen(onTimeout = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }

        // 2. Tela de Login
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToCadastro = {
                    navController.navigate(Screen.Cadastro.route)
                }
            )
        }

        composable(Screen.Cadastro.route) {
            CadastroScreen(
                onCadastroSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Cadastro.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // 4. Tela Principal (Dashboard Firebase CRUD)
        composable(Screen.Main.route) {
            val currentUid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""


            key(currentUid) {
                MainScreen(
                    onLogout = {

                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Main.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}