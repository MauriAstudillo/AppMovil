package com.example.ejemplonavycomp.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Interfaz que define los endpoints de la API.
 */
interface Tiendalvlup {
    /**
     * Define una función suspendida para obtener las categorías.
     * La anotación @GET especifica la ruta del endpoint relativa a la URL base.
     */
    @GET("2c21923f0c7c8ce15f8b12f5121cedef/raw/8216ae320580b192f657788ac73b9466451621a5/productos.json")
    suspend fun getCategories(): CategoryResponse
}

/**
 * Objeto singleton para crear y proveer una instancia de Retrofit.
 * Esto asegura que solo tengamos una instancia de Retrofit para toda la app.
 */
object RetrofitInstance {
    private const val BASE_URL = "https://gist.githubusercontent.com/MauriAstudillo/"

    // Usamos 'lazy' para que la instancia de Retrofit solo se cree
    // la primera vez que se accede a 'api'.
    val api: Tiendalvlup by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // Agregamos un convertidor de Gson para parsear el JSON
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            // Creamos una implementación de nuestra interfaz ApiService
            .create(Tiendalvlup::class.java)
    }
}