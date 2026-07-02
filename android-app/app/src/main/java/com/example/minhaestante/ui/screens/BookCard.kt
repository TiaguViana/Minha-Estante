package com.example.minhaestante.ui.screens

//////////////////////////////// imports //////////////////////////////////////
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.minhaestante.R
import com.example.minhaestante.ui.theme.AtkinsonHyperlegible
import com.example.minhaestante.ui.theme.Baskervville
import com.example.minhaestante.ui.theme.BuscaBordaDark
import com.example.minhaestante.ui.theme.BuscaBordaLight
import com.example.minhaestante.ui.theme.DarkSurface
import com.example.minhaestante.ui.theme.DarkTextPrimary
import com.example.minhaestante.ui.theme.DarkTextSecondary
import com.example.minhaestante.ui.theme.EstanteSecretaPrimaryLight
import com.example.minhaestante.ui.theme.EstanteSecretaSecondaryLight
import com.example.minhaestante.ui.theme.LightSurface
import com.example.minhaestante.ui.theme.LightTextPrimary
import com.example.minhaestante.ui.theme.LightTextSecondary
import com.example.minhaestante.ui.theme.MeusLivrosPrimaryLight
import com.example.minhaestante.ui.theme.MeusLivrosSecondaryLight
import com.example.minhaestante.ui.theme.StatusConcluidoLight
import com.example.minhaestante.ui.theme.StatusEmAndamento
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
    val context = LocalContext.current
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

    val dateFormat = remember { SimpleDateFormat("dd/MM", Locale.getDefault()) }

    // Isolamento do estado do favorito para forçar a re-recomposição imediata do ícone
    val isFavoriteState = remember(book.isFavorite) { book.isFavorite }

    // Tratamento de strings fallback para evitar quebras visuais e falhas de tradução
    val tituloTratado = book.title.ifEmpty { stringResource(id = R.string.label_sem_titulo) }
    val autorTratado = book.author.ifEmpty { stringResource(id = R.string.label_autor_desconhecido) }
    val descricaoTratada = book.description.ifEmpty { stringResource(id = R.string.label_sem_descricao) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = cardBackground,
            title = {
                Text(
                    text = stringResource(id = R.string.dialog_excluir_titulo),
                    fontFamily = Baskervville,
                    fontWeight = FontWeight.Bold,
                    color = textPrimaryColor
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.dialog_excluir_mensagem),
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
                    Text(
                        text = stringResource(id = R.string.btn_sim),
                        fontFamily = AtkinsonHyperlegible,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(
                        text = stringResource(id = R.string.btn_cancelar),
                        fontFamily = AtkinsonHyperlegible,
                        color = textSecondaryColor
                    )
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
                    contentDescription = stringResource(id = R.string.logo_desc) + ": $tituloTratado",
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
                // Linha Superior: Agrupamento de Ações Rápidas com Semântica de Acessibilidade Correta
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val descFavorito = stringResource(
                        id = if (isFavoriteState) R.string.desc_desfavoritar_livro else R.string.desc_favoritar_livro,
                        tituloTratado
                    )
                    Icon(
                        imageVector = if (isFavoriteState) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = descFavorito,
                        tint = if (isFavoriteState) StatusConcluidoLight else textSecondaryColor.copy(alpha = 0.6f),
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(
                                role = Role.Button,
                                onClickLabel = descFavorito,
                                onClick = onFavoriteClick
                            )
                    )

                    val descRemover = stringResource(id = R.string.desc_remover_livro, tituloTratado)
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = descRemover,
                        tint = textSecondaryColor.copy(alpha = 0.6f),
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(
                                role = Role.Button,
                                onClickLabel = descRemover,
                                onClick = { showDeleteDialog = true }
                            )
                    )
                }

                // Título, Autor e Data
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = tituloTratado,
                            fontFamily = Baskervville,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimaryColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = autorTratado,
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
                    text = descricaoTratada,
                    fontFamily = AtkinsonHyperlegible,
                    fontSize = 13.sp,
                    color = textPrimaryColor.copy(alpha = 0.9f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Indicador de Status com Semântica Unificada para Leitores de Tela
                val descStatusEspeficico = when (book.readingStatus) {
                    ReadingStatus.CONCLUIDO -> stringResource(id = R.string.desc_livro_status_concluido)
                    ReadingStatus.EM_ANDAMENTO -> stringResource(id = R.string.desc_livro_status_andamento)
                }
                Row(
                    modifier = Modifier.semantics(mergeDescendants = true) {
                        contentDescription = "$descStatusEspeficico: ${book.readingStatus.label}"
                    },
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

                // Rodapé: Avaliação em Estrelas (Limpa e Acessível) + Ação de Editar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val labelAvaliacao = stringResource(id = R.string.desc_avaliacao_livro, book.rating)
                    Row(
                        modifier = Modifier.clearAndSetSemantics {
                            contentDescription = labelAvaliacao
                        },
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        for (i in 1..5) {
                            val starColor = if (i <= book.rating) Color(0xFFC2D62E) else goldAccent
                            Text(
                                text = "★",
                                fontSize = 18.sp,
                                color = starColor
                            )
                        }
                    }

                    val descEditar = stringResource(id = R.string.desc_editar_livro, tituloTratado)
                    Text(
                        text = stringResource(id = R.string.btn_editar),
                        fontFamily = AtkinsonHyperlegible,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (section == BookSection.MEUS_LIVROS) MeusLivrosPrimaryLight else EstanteSecretaPrimaryLight,
                        modifier = Modifier
                            .clickable(
                                role = Role.Button,
                                onClickLabel = descEditar,
                                onClick = onEditClick
                            )
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}