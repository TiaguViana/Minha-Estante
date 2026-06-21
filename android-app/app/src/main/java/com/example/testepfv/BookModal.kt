package com.example.testepfv

import android.app.DatePickerDialog
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testepfv.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.runtime.mutableIntStateOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookModal(
    bookToEdit: Book?,
    section: BookSection,
    onDismiss: () -> Unit,
    onSave: (title: String, author: String, description: String, status: ReadingStatus, rating: Int, date: Date, imageUrl: String?) -> Unit
) {
    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()
    val coroutineScope = rememberCoroutineScope()

    val accentColor = if (section == BookSection.MEUS_LIVROS) {
        if (isDark) MeusLivrosSecondaryLight else MeusLivrosPrimaryLight
    } else {
        if (isDark) EstanteSecretaSecondaryLight else EstanteSecretaPrimaryLight
    }

    val modalBackground = if (isDark) DarkSurface else LightSurface
    val textPrimaryColor = if (isDark) DarkTextPrimary else LightTextPrimary
    val textSecondaryColor = if (isDark) DarkTextSecondary else LightTextSecondary
    val goldAccent = if (isDark) BuscaBordaDark else BuscaBordaLight
    val goldBadgeBg = if (isDark) BuscaPreenchimentoDark else BuscaPreenchimentoLight

    var title by remember { mutableStateOf(bookToEdit?.title ?: "") }
    var author by remember { mutableStateOf(bookToEdit?.author ?: "") }
    var description by remember { mutableStateOf(bookToEdit?.description ?: "") }

    var apiSearchQuery by remember { mutableStateOf("") }
    var isSearchingByApi by remember { mutableStateOf(false) }
    var searchJob by remember { mutableStateOf<Job?>(null) }
    var imageUrl by remember { mutableStateOf(bookToEdit?.imageUrl ?: "") }

    var apiResults by remember { mutableStateOf(listOf<ApiBookResult>()) }

    var status by remember { mutableStateOf(bookToEdit?.readingStatus ?: ReadingStatus.CONCLUIDO) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var rating by remember { mutableIntStateOf(bookToEdit?.rating ?: 3) }

    val calendar = remember { Calendar.getInstance().apply { bookToEdit?.readingDate?.let { time = it } } }
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    var dateString by remember { mutableStateOf(sdf.format(calendar.time)) }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            dateString = sdf.format(calendar.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = modalBackground,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = null,
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, accentColor, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            ) {
                OutlinedTextField(
                    value = apiSearchQuery,
                    onValueChange = { query ->
                        apiSearchQuery = query
                        searchJob?.cancel()

                        if (query.isNotBlank() && query.trim().length >= 3) {
                            searchJob = coroutineScope.launch {
                                delay(800)
                                isSearchingByApi = true

                                try {
                                    val apiService = GoogleBooksApiService.create()
                                    val response = apiService.searchBooks(
                                        query = query.trim(),
                                        apiKey = GoogleBooksApiService.MINHA_API_KEY
                                    )

                                    apiResults = response.items?.map { item ->
                                        ApiBookResult(
                                            titulo = item.volumeInfo.title,
                                            autor = item.volumeInfo.authors?.joinToString(", ") ?: "Autor Desconhecido",
                                            descricao = item.volumeInfo.description ?: "Nenhuma sinopse disponível.",
                                            imageUrl = item.volumeInfo.imageLinks?.thumbnail ?: ""
                                        )
                                    } ?: emptyList()

                                } catch (e: retrofit2.HttpException) {
                                    if (e.code() == 429) {
                                        println("DEBUG_API: Bloqueio 429 temporário por excesso de requisições.")
                                    } else {
                                        println("DEBUG_API: Erro HTTP do servidor -> ${e.code()}")
                                    }
                                    apiResults = emptyList()
                                } catch (e: Exception) {
                                    println("DEBUG_API: Falha de rede -> ${e.message}")
                                    apiResults = emptyList()
                                } finally {
                                    isSearchingByApi = false
                                }
                            }
                        } else {
                            apiResults = emptyList()
                        }
                    },
                    placeholder = { Text("Buscar livro na API...", color = textSecondaryColor) },
                    leadingIcon = {
                        if (isSearchingByApi) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = goldAccent)
                        } else {
                            Icon(Icons.Default.Search, contentDescription = "Buscar via API", tint = goldAccent)
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = goldAccent,
                        unfocusedBorderColor = goldAccent.copy(alpha = 0.7f),
                        focusedTextColor = textPrimaryColor,
                        unfocusedTextColor = textPrimaryColor,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                )

                if (apiResults.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .heightIn(min = 100.dp, max = 220.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDark) Color(0xFF1E1E1E) else Color(0xFFF9F9F9)
                        ),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            items(apiResults) { bookResult ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            title = bookResult.titulo
                                            author = bookResult.autor
                                            description = bookResult.descricao
                                            imageUrl = bookResult.imageUrl
                                            apiResults = emptyList()
                                            apiSearchQuery = ""
                                        }
                                        .padding(vertical = 12.dp, horizontal = 12.dp)
                                ) {
                                    Text(
                                        text = bookResult.titulo,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = textPrimaryColor,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = bookResult.autor,
                                        fontSize = 13.sp,
                                        color = textSecondaryColor,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                HorizontalDivider(color = textSecondaryColor.copy(alpha = 0.15f))
                            }
                        }
                    }
                }
            }

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título do Livro") },
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontFamily = Baskervville, fontSize = 22.sp, fontWeight = FontWeight.Bold),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = accentColor, unfocusedBorderColor = textSecondaryColor.copy(0.4f)),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Autor") },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = AtkinsonHyperlegible),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = accentColor, unfocusedBorderColor = textSecondaryColor.copy(0.4f)),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descrição / Sinopse") },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = AtkinsonHyperlegible),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = accentColor, unfocusedBorderColor = textSecondaryColor.copy(0.4f)),
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            Box {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(goldBadgeBg)
                        .clickable { dropdownExpanded = true }
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = status.label, color = LightTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp, fontFamily = AtkinsonHyperlegible)
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Expandir opções", tint = LightTextPrimary, modifier = Modifier.size(18.dp))
                    }
                }

                DropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false },
                    modifier = Modifier.background(modalBackground)
                ) {
                    ReadingStatus.entries.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(text = option.label, color = textPrimaryColor, fontFamily = AtkinsonHyperlegible, fontWeight = FontWeight.Medium) },
                            onClick = {
                                status = option
                                dropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                for (i in 1..5) {
                    val starColor = if (i <= rating) Color(0xFFC2D62E) else BuscaBordaDark
                    Text(
                        text = "★",
                        fontSize = 26.sp,
                        color = starColor,
                        modifier = Modifier.clickable { rating = i }
                    )
                }
            }
            Column {
                Text(text = "Data de Leitura", fontSize = 14.sp, fontFamily = AtkinsonHyperlegible, color = textPrimaryColor, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = dateString,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { datePickerDialog.show() }) {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Abrir calendário", tint = goldAccent)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = goldAccent,
                        unfocusedBorderColor = goldAccent.copy(alpha = 0.5f),
                        focusedTextColor = textPrimaryColor,
                        unfocusedTextColor = textPrimaryColor
                    ),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cancelar",
                    fontFamily = AtkinsonHyperlegible,
                    color = accentColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onDismiss() }
                )

                Text(
                    text = "Salvar",
                    fontFamily = AtkinsonHyperlegible,
                    color = goldAccent,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        if (title.isNotEmpty()) {
                            onSave(title, author, description, status, rating, calendar.time, imageUrl.ifBlank { null })
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

data class ApiBookResult(val titulo: String, val autor: String, val descricao: String, val imageUrl: String)

private suspend fun buscarLivrosNaApiReal(nomeLivro: String): List<ApiBookResult>? = withContext(Dispatchers.IO) {
    val listaResultados = mutableListOf<ApiBookResult>()
    var urlConnection: HttpURLConnection? = null

    try {
        val queryAdulterada = URLEncoder.encode(nomeLivro.trim(), "UTF-8")
        val url = URL("https://www.googleapis.com/books/v1/volumes?q=$queryAdulterada&maxResults=5")

        urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.connectTimeout = 8000
        urlConnection.readTimeout = 8000
        urlConnection.requestMethod = "GET"

        urlConnection.setRequestProperty("Accept", "application/json")
        urlConnection.setRequestProperty("Accept-Encoding", "identity")

        val responseCode = urlConnection.responseCode

        if (responseCode == HttpURLConnection.HTTP_OK) {
            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream, "UTF-8"))
            val response = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()

            val jsonObject = JSONObject(response.toString())
            if (jsonObject.has("items")) {
                val items = jsonObject.getJSONArray("items")
                for (i in 0 until items.length()) {
                    val volumeInfo = items.getJSONObject(i).getJSONObject("volumeInfo")

                    val titulo = volumeInfo.optString("title", "Sem Título")
                    val autoresArray = volumeInfo.optJSONArray("authors")
                    val autor = if (autoresArray != null && autoresArray.length() > 0) {
                        autoresArray.getString(0)
                    } else {
                        "Autor Desconhecido"
                    }
                    val textoDescricao = volumeInfo.optString("description", "Nenhuma sinopse disponível para este livro.")

                    val imageLinks = volumeInfo.optJSONObject("imageLinks")
                    val thumb = imageLinks?.optString("thumbnail") ?: ""

                    listaResultados.add(
                        ApiBookResult(
                            titulo = titulo,
                            autor = autor,
                            descricao = textoDescricao,
                            imageUrl = thumb
                        )
                    )
                }
                return@withContext listaResultados
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        urlConnection?.disconnect()
    }
    return@withContext null
}