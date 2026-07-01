package com.example.minhaestante.data

import com.google.firebase.Timestamp

data class User(
    val nome: String = "",
    val email: String = "",
    val status: String = "ativo", // 'ativo' | 'inativo'
    val cadastro: Timestamp = Timestamp.now(),
    val isAdmin: Boolean = false
)