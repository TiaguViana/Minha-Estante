package com.example.testepfv

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.testepfv.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun BookCard(
    book: Book,
    section: BookSection,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    var showDeleteDialog by remember { mutableStateOf(false) }

    val borderColor = if (section == BookSection.MEUS_LIVROS) {
        if (isDark) MeusLivrosSecondaryLight else MeusLivrosPrimaryLight
    } else {
        if (isDark) EstanteSecretaSecondaryLight else EstanteSecretaPrimaryLight
    }

    val cardBackground = if (isDark) DarkSurface else LightSurface
    val textPrimaryColor = if (isDark) DarkTextPrimary else LightTextPrimary
    val textSecondaryColor = if (isDark) DarkTextSecondary else LightTextSecondary
    val goldAccent = if (isDark) BuscaBordaDark else BuscaBordaLight

    val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = cardBackground,
            title = {
                Text(
                    text = "Excluir Livro",
                    fontFamily = Baskervville,
                    fontWeight = FontWeight.Bold,
                    color = textPrimaryColor
                )
            },
            text = {
                Text(
                    text = "Tem certeza que deseja remover este livro da sua lista?",
                    fontFamily = AtkinsonHyperlegible,
                    color = textSecondaryColor
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick()
                    }
                ) {
                    Text("Sim", fontFamily = AtkinsonHyperlegible, color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar", fontFamily = AtkinsonHyperlegible, color = textSecondaryColor)
                }
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(2.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            if (!book.imageUrl.isNullOrBlank()) {
                val seguraImageUrl = book.imageUrl.replace("http://", "https://")

                AsyncImage(
                    model = seguraImageUrl,
                    contentDescription = "Capa do livro ${book.title}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(75.dp)
                        .height(110.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(textSecondaryColor.copy(alpha = 0.1f))
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (book.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = StatusConcluidoLight,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onFavoriteClick() }
                    )
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remover",
                        tint = textSecondaryColor.copy(alpha = 0.6f),
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { showDeleteDialog = true }
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = book.title.ifEmpty { "Sem Título" },
                            fontFamily = Baskervville,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimaryColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = book.author.ifEmpty { "Autor Desconhecido" },
                            fontFamily = AtkinsonHyperlegible,
                            fontSize = 13.sp,
                            color = textSecondaryColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        text = dateFormat.format(book.readingDate),
                        fontFamily = AtkinsonHyperlegible,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = borderColor,
                        modifier = Modifier.padding(start = 6.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = book.description.ifEmpty { "Nenhuma descrição informada para este livro." },
                    fontFamily = AtkinsonHyperlegible,
                    fontSize = 13.sp,
                    color = textPrimaryColor.copy(alpha = 0.9f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val circleColor = when (book.readingStatus) {
                        ReadingStatus.CONCLUIDO -> StatusConcluidoLight
                        ReadingStatus.EM_ANDAMENTO -> StatusEmAndamento
                    }
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(circleColor)
                    )
                    Text(
                        text = book.readingStatus.label,
                        fontFamily = AtkinsonHyperlegible,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimaryColor
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        for (i in 1..5) {
                            val starColor = if (i <= book.rating) Color(0xFFC2D62E) else goldAccent
                            Text(
                                text = "★",
                                fontSize = 18.sp,
                                color = starColor
                            )
                        }
                    }

                    Text(
                        text = "Editar",
                        fontFamily = AtkinsonHyperlegible,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (section == BookSection.MEUS_LIVROS) MeusLivrosPrimaryLight else EstanteSecretaPrimaryLight,
                        modifier = Modifier
                            .clickable { onEditClick() }
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}