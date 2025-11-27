package com.example.ejemplonavycomp.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Interfaz que define los endpoints de la API.
 */
interface TheMealDbApiService {
    /**
     * Define una función suspendida para obtener las categorías.
     * La anotación @GET especifica la ruta del endpoint relativa a la URL base.
     */
    @GET("api/json/v1/1/categories.php")
    suspend fun getCategories(): CategoryResponse
}

/**
 * Objeto singleton para crear y proveer una instancia de Retrofit.
 * Esto asegura que solo tengamos una instancia de Retrofit para toda la app.
 */
object RetrofitInstance {
    private const val BASE_URL = "https://www.themealdb.com/"

    // Usamos 'lazy' para que la instancia de Retrofit solo se cree
    // la primera vez que se accede a 'api'.
    val api: TheMealDbApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // Agregamos un convertidor de Gson para parsear el JSON
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            // Creamos una implementación de nuestra interfaz ApiService
            .create(TheMealDbApiService::class.java)
    }
}