package com.example.minhaestante

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.example.minhaestante.ui.screens.MainScreen
import com.example.minhaestante.ui.AppNavigation
import com.example.minhaestante.ui.theme.TestePfvTheme // <-- IMPORTANTE: Import do seu tema
import com.google.firebase.FirebaseApp

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        FirebaseApp.initializeApp(this)

        setContent {
            TestePfvTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}