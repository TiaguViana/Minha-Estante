package com.example.testepfv

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testepfv.BiometricHelper.autenticar
import com.example.testepfv.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()

    val backgroundColor = if (isDark) DarkBackground else LightBackground
    val textPrimaryColor = if (isDark) DarkTextPrimary else LightTextPrimary
    val textSecondaryColor = if (isDark) DarkTextSecondary else LightTextSecondary
    val goldAccent = if (isDark) BuscaBordaDark else BuscaBordaLight

    var booksList by remember { mutableStateOf(listOf<Book>()) }
    var selectedTab by remember { mutableStateOf(0) } // 0 = Meus Livros, 1 = Estante Secreta
    var searchQuery by remember { mutableStateOf("") }
    var isDescendingOrder by remember { mutableStateOf(true) }
    var filterOnlyFavorites by remember { mutableStateOf(false) }
    val currentSection = if (selectedTab == 0) BookSection.MEUS_LIVROS else BookSection.ESTANTE_SECRETA
    val filteredBooks = booksList.filter { book ->
        val matchesTab = book.section == currentSection
        val matchesSearch = book.title.contains(searchQuery, ignoreCase = true) ||
                book.author.contains(searchQuery, ignoreCase = true)
        val matchesFavorite = if (filterOnlyFavorites) book.isFavorite else true
        matchesTab && matchesSearch && matchesFavorite
    }

    val sortedBooks = filteredBooks.sortedWith { b1, b2 ->
        if (isDescendingOrder) {
            b2.readingDate.compareTo(b1.readingDate) // Mais Recentes
        } else {
            b1.readingDate.compareTo(b2.readingDate) // Mais Antigos
        }
    }

    val groupFormatter = SimpleDateFormat("MMMM yyyy", Locale("pt", "BR"))
    val groupedBooks = sortedBooks.groupBy { book ->
        groupFormatter.format(book.readingDate).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

    var showModal by remember { mutableStateOf(false) }
    var bookToEdit by remember { mutableStateOf<Book?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Olá, ",
                    fontSize = 28.sp,
                    fontFamily = Baskervville,
                    color = textPrimaryColor
                )
                Box(
                    modifier = Modifier
                        .background(CorDestaque, RoundedCornerShape(2.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Sabrina!",
                        fontSize = 28.sp,
                        fontFamily = Baskervville,
                        fontWeight = FontWeight.Bold,
                        color = LightTextPrimary
                    )
                }
            }
            Text(
                text = "Como andam suas leituras?",
                fontSize = 16.sp,
                fontFamily = AtkinsonHyperlegible,
                color = textPrimaryColor,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedTab = 0 }
                ) {
                    Text(
                        text = "Meus Livros",
                        fontSize = 16.sp,
                        fontFamily = AtkinsonHyperlegible,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedTab == 0) (if (isDark) MeusLivrosSecondaryLight else MeusLivrosPrimaryLight) else textPrimaryColor
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(2.dp)
                            .background(if (selectedTab == 0) (if (isDark) MeusLivrosSecondaryLight else MeusLivrosPrimaryLight) else Color.Transparent)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            autenticar(
                                context = context,
                                onSucesso = { selectedTab = 1 },
                                onErro = { selectedTab = 0 }
                            )
                        }
                ) {
                    Text(
                        text = "Estante Secreta",
                        fontSize = 16.sp,
                        fontFamily = AtkinsonHyperlegible,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedTab == 1) (if (isDark) EstanteSecretaSecondaryLight else EstanteSecretaPrimaryLight) else textPrimaryColor
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(2.dp)
                            .background(if (selectedTab == 1) (if (isDark) EstanteSecretaSecondaryLight else EstanteSecretaPrimaryLight) else Color.Transparent)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val placeholderColor = if (isDark) BuscaPreenchimentoDark else BuscaPreenchimentoLight

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Buscar", color = placeholderColor) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar", tint = goldAccent) },
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = textPrimaryColor,
                        unfocusedTextColor = textPrimaryColor,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .border(2.dp, if (searchQuery.isNotEmpty()) goldAccent else goldAccent.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
                )

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .border(1.5.dp, goldAccent, RoundedCornerShape(22.dp))
                        .clickable {
                            bookToEdit = null
                            showModal = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar", tint = goldAccent, modifier = Modifier.size(24.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedTab == 0) "Meus Livros" else "Estante Secreta",
                    fontSize = 22.sp,
                    fontFamily = Baskervville,
                    fontWeight = FontWeight.Bold,
                    color = textPrimaryColor
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Icon(
                        imageVector = if (filterOnlyFavorites) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Filtrar favoritos",
                        tint = if (filterOnlyFavorites) goldAccent else CorDestaque,
                        modifier = Modifier
                            .size(22.dp)
                            .clickable { filterOnlyFavorites = !filterOnlyFavorites }
                    )

                    Row(
                        modifier = Modifier.clickable { isDescendingOrder = !isDescendingOrder },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = if (isDescendingOrder) "Recentes" else "Antigos",
                            fontSize = 16.sp,
                            fontFamily = AtkinsonHyperlegible,
                            fontWeight = FontWeight.Medium,
                            color = CorDestaque
                        )
                        Icon(
                            imageVector = if (isDescendingOrder) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                            contentDescription = "Mudar Ordenação",
                            tint = goldAccent,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                groupedBooks.forEach { (monthHeader, booksInMonth) ->
                    item {
                        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Text(
                                text = monthHeader,
                                fontSize = 14.sp,
                                fontFamily = AtkinsonHyperlegible,
                                color = textPrimaryColor,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(textSecondaryColor.copy(alpha = 0.3f)))
                        }
                    }

                    items(booksInMonth) { book ->
                        BookCard(
                            book = book,
                            section = currentSection,
                            onEditClick = {
                                bookToEdit = book
                                showModal = true
                            },
                            onDeleteClick = {
                                booksList = booksList.filter { it.id != book.id }
                            },
                            onFavoriteClick = {
                                booksList = booksList.map {
                                    if (it.id == book.id) it.copy(isFavorite = !it.isFavorite) else it
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    if (showModal) {
        BookModal(
            bookToEdit = bookToEdit,
            section = currentSection,
            onDismiss = { showModal = false },
            onSave = { title, author, desc, status, rating, date, imageUrl ->
                if (bookToEdit == null) {
                    val newBook = Book(
                        id = UUID.randomUUID().toString(),
                        title = title,
                        author = author,
                        description = desc,
                        readingStatus = status,
                        rating = rating,
                        readingDate = date,
                        imageUrl = imageUrl,
                        isFavorite = false,
                        section = currentSection
                    )
                    booksList = booksList + newBook
                } else {
                    booksList = booksList.map {
                        if (it.id == bookToEdit!!.id) it.copy(
                            title = title,
                            author = author,
                            description = desc,
                            readingStatus = status,
                            rating = rating,
                            readingDate = date,
                            imageUrl = imageUrl
                        ) else it
                    }
                }
                showModal = false
            }
        )
    }
}