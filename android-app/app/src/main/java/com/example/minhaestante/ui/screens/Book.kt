package com.example.minhaestante.ui.screens

import java.util.Date
import java.util.UUID

enum class ReadingStatus(val label: String) {
    CONCLUIDO("Concluído"),
    EM_ANDAMENTO("Em Andamento")
}

enum class BookSection {
    MEUS_LIVROS,
    ESTANTE_SECRETA
}

data class Book(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val author: String = "",
    val description: String = "",
    val readingStatus: ReadingStatus = ReadingStatus.CONCLUIDO,
    val rating: Int = 3,
    val readingDate: Date = Date(),
    val imageUrl: String? = null,
    val isFavorite: Boolean = false,
    val section: BookSection = BookSection.MEUS_LIVROS
)