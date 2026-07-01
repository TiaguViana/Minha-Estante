package com.example.testepfv

import java.util.Date


enum class BookSection {
    MEUS_LIVROS,
    ESTANTE_SECRETA
}

enum class ReadingStatus(val label: String) {
    EM_ANDAMENTO("Lendo"),
    CONCLUIDO("Concluído")
}

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val description: String,
    val readingStatus: ReadingStatus,
    val rating: Int,
    val readingDate: Date,
    val imageUrl: String? = null,
    val isFavorite: Boolean = false,
    val section: BookSection
)