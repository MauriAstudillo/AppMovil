package com.example.ejemplonavycomp.data

import com.google.gson.annotations.SerializedName

data class CategoryResponse
    (
    // "@SerializedName" asegura que Gson mapee la clave "categories" del JSON
    // a esta propiedad, incluso si tuviéramos un nombre de variable diferente.
    @SerializedName("categories")
    val categories: List<Category>
)

/**
 * Representa una única categoría de comida.
 * eventualmente podemos separarlo en otro archivo, pero los dejaremos aquí porque
 * trabajan en conjunto
 */
data class Category(
    @SerializedName("idCategory")
    val id: String,

    @SerializedName("strCategory")
    val name: String,

    @SerializedName("strCategoryThumb")
    val thumbnail: String,

    @SerializedName("strCategoryDescription")
    val description: String,

    @SerializedName("price")
    val price: String
)