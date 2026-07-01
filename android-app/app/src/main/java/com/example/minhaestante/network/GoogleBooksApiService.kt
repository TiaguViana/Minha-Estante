package com.example.minhaestante.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class GoogleBooksResponse(val items: List<BookItem>?)
data class BookItem(val id: String, val volumeInfo: VolumeInfo)
data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val description: String?,
    val imageLinks: ImageLinks?
)
data class ImageLinks(val thumbnail: String?)

interface GoogleBooksApiService {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 5,
        @Query("key") apiKey: String
    ): GoogleBooksResponse

    companion object {
        private const val BASE_URL = "https://www.googleapis.com/books/v1/"

        const val MINHA_API_KEY = "AIzaSyCFuNH7gH5wGUguYmVxfu-S6yApTxuWQno"

        fun create(): GoogleBooksApiService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .header("Accept", "application/json")
                        .build()
                    chain.proceed(request)
                }
                .retryOnConnectionFailure(true)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GoogleBooksApiService::class.java)
        }
    }
}