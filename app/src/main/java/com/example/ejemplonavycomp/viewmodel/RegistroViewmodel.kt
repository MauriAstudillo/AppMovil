
package com.example.ejemplonavycomp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ejemplonavycomp.data.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RegistroViewModel(context: Context) : ViewModel() {

    private val prefs = UserPreferences(context)

    // --- Usuario actual ---
    val currentUserEmail = prefs.getCurrentUserEmail.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )

    val currentUserPassword = prefs.getCurrentUserPassword.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )

    // --- Carrito ---
    val cartItems = prefs.getCartItems.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    // --- Foto de perfil 🖼 ---
    val profilePictureUri = prefs.profilePictureUri.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )

    fun updateProfilePicture(uri: String) {
        viewModelScope.launch {
            prefs.saveProfilePicture(uri)
        }
    }

    // --- Registro / login ---
    fun saveUser(email: String, password: String) {
        viewModelScope.launch {
            prefs.saveUser(email, password)
        }
    }

    fun setCurrentUser(email: String) {
        viewModelScope.launch {
            prefs.setCurrentUser(email)
        }
    }

    fun logout() {
        viewModelScope.launch {
            prefs.logout()
        }
    }

    suspend fun getPasswordForUser(email: String): String? {
        return prefs.getPasswordForUser(email)
    }

    // --- Carrito acciones ---
    fun addToCart(productName: String) {
        viewModelScope.launch {
            prefs.addToCart(productName)
        }
    }

    fun removeFromCart(productName: String) {
        viewModelScope.launch {
            prefs.removeFromCart(productName)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            prefs.clearCart()
        }
    }
}