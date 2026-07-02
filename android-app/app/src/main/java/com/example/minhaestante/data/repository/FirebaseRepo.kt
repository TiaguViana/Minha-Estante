package com.example.minhaestante.data.repository

import com.example.minhaestante.data.User
import com.example.minhaestante.ui.screens.Book
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseRepo {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // --- AUTENTICAÇÃO E PERFIL ---

    fun cadastrarUsuario(
        nome: String,
        email: String,
        senha: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, senha)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid
                if (uid != null) {
                    val novoUsuario = User(
                        nome = nome,
                        email = email,
                        status = "ativo",
                        cadastro = Timestamp.now(),
                        isAdmin = false
                    )
                    firestore.collection("usuarios").document(uid).set(novoUsuario)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { e -> onFailure("Erro ao salvar: ${e.localizedMessage}") }
                }
            }
            .addOnFailureListener { e -> onFailure("Erro: ${e.localizedMessage}") }
    }

    fun loginUsuario(email: String, senha: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onSuccess() else onFailure(task.exception?.message ?: "Erro ao entrar.")
            }
    }

    // --- ESTANTE DE LIVROS (CRUD COMPLETO) ---

    // Busca contínua e reativa
    fun getBooks(): Flow<List<Book>> = callbackFlow {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val listener = firestore.collection("usuarios").document(userId).collection("livros")
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    val books = snapshot.toObjects(Book::class.java)
                    trySend(books)
                }
            }

        awaitClose { listener.remove() }
    }

    // Salva ou atualiza um livro usando o ID gerado pelo seu UUID
    fun saveBook(book: Book) {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("usuarios").document(userId)
            .collection("livros").document(book.id).set(book)
            .addOnFailureListener { e ->
                // Se a escrita for rejeitada (ex: regra de segurança do Firestore),
                // o cache local reverte sozinho e a UI "volta atrás" sem aviso nenhum.
                // Logamos aqui pra dar visibilidade em caso de falha.
                Log.e("FirebaseRepo", "Falha ao salvar livro ${book.id}", e)
            }
    }

    // Apaga um livro
    fun deleteBook(bookId: String) {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("usuarios").document(userId)
            .collection("livros").document(bookId).delete()
            .addOnFailureListener { e ->
                Log.e("FirebaseRepo", "Falha ao apagar livro $bookId", e)
            }
    }
}