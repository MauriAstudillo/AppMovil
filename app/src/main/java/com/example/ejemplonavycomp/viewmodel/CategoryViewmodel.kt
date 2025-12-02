package com.example.ejemplonavycomp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ejemplonavycomp.data.CategoryResponse
import com.example.ejemplonavycomp.data.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * Define los posibles estados de la UI para la pantalla de categorías.
 * Usar una 'sealed interface' (o sealed class) es una buena práctica
 * para manejar estados en el Composable.
 */
sealed interface CategoryUiState {
    data class Success(val response: CategoryResponse) : CategoryUiState
    data class Error(val message: String) : CategoryUiState
    object Loading : CategoryUiState
}

class CategoryViewModel : ViewModel() {

    // Flujo de estado mutable y privado para actualizar el estado internamente.
    private val _uiState = MutableStateFlow<CategoryUiState>(CategoryUiState.Loading)

    // Flujo de estado público e inmutable para que la UI observe los cambios.
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    // El bloque 'init' se llama cuando el ViewModel es creado.
    init {
        fetchCategories()
    }

    /**
     * Lanza una corutina en el 'viewModelScope' para obtener los datos
     * de la API de forma asíncrona y segura.
     */
    fun fetchCategories() {
        // Inicia mostrando el estado de Carga
        _uiState.value = CategoryUiState.Loading

        viewModelScope.launch {
            try {
                // Llama a la API a través de nuestra instancia de Retrofit
                val categoryResponse = RetrofitInstance.api.getCategories()
                // Actualiza el estado a Success con los datos recibidos
                _uiState.value = CategoryUiState.Success(categoryResponse)
            } catch (e: IOException) {
                // Maneja errores de red (ej. sin conexión)
                _uiState.value = CategoryUiState.Error("Error de red: ${e.message}")
            } catch (e: Exception) {
                // Maneja otros errores (ej. parseo de JSON, errores de servidor)
                _uiState.value = CategoryUiState.Error("Error desconocido: ${e.message}")
            }
        }
    }
}