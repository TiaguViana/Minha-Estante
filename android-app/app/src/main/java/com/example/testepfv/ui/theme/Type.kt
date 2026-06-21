package com.example.testepfv.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.testepfv.R // Importa os recursos do seu novo pacote correto

val Baskervville = FontFamily(
    Font(R.font.baskerregular, FontWeight.Normal)
)

val AtkinsonHyperlegible = FontFamily(
    Font(R.font.hyperregular, FontWeight.Normal),
    Font(R.font.hyperbold, FontWeight.Bold)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = Baskervville,
        fontWeight = FontWeight.Normal,
        fontSize = 40.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Baskervville,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Baskervville,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = AtkinsonHyperlegible,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelMedium = TextStyle(
        fontFamily = AtkinsonHyperlegible,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )
)