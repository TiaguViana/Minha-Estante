package com.example.minhaestante.data

import com.example.minhaestante.ui.screens.Book
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class BookRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Retorna o ID do usuário logado (útil se tiver tela de login com Firebase Auth)
    // Se não tiver, usa um nó fixo para os testes
    private val userId: String
        get() = auth.currentUser?.uid ?: "usuario_sabrina"

    private val booksCollection = firestore
        .collection("users")
        .document(userId)
        .collection("books")

    // LER
    fun getBooks(): Flow<List<Book>> {
        return booksCollection.snapshots().map { snapshot ->
            snapshot.toObjects(Book::class.java)
        }
    }

    // CRIAR / ATUALIZAR
    suspend fun saveBook(book: Book) {
        try {
            booksCollection.document(book.id).set(book).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // DELETAR
    suspend fun deleteBook(bookId: String) {
        try {
            booksCollection.document(bookId).delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}