package com.example.minhaestante.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.example.minhaestante.R


@Composable
fun SplashScreen(onTimeout: () -> Unit) {

    // Dark Mode
    val darkTheme = isSystemInDarkTheme()

    LaunchedEffect(Unit) {
        delay(3000) // 3 segundos
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = true,
            enter = fadeIn()
        ) {
            Image(
                painter = painterResource(id = if (darkTheme) R.drawable.logindark else R.drawable.logo),
                contentDescription = "Imagem da logo do Minha Estante",
                modifier = Modifier
                    .width(240.dp)
                    .height(110.dp)
            )
        }
    }
}