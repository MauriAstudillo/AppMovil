package com.example.ejemplonavycomp.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val USERS_JSON = stringPreferencesKey("users_json")
        val CURRENT_USER_EMAIL = stringPreferencesKey("current_user")
        val CART_JSON = stringPreferencesKey("cart_json")

        // 🆕 Historial de compras por usuario
        val PURCHASE_HISTORY_JSON = stringPreferencesKey("purchase_history_json")

        // 🖼 URI de la foto de perfil (global por ahora)
        val PROFILE_PICTURE_URI = stringPreferencesKey("profile_picture_uri")
    }

    // --- Usuarios ---
    suspend fun saveUser(email: String, password: String) {
        context.dataStore.edit { prefs ->
            val jsonString = prefs[USERS_JSON]
            val users = if (jsonString != null) JSONObject(jsonString) else JSONObject()
            users.put(email, password)
            prefs[USERS_JSON] = users.toString()
        }
    }

    suspend fun setCurrentUser(email: String) {
        context.dataStore.edit { prefs ->
            prefs[CURRENT_USER_EMAIL] = email
        }
    }

    val getCurrentUserEmail: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[CURRENT_USER_EMAIL]
    }

    val getCurrentUserPassword: Flow<String?> = context.dataStore.data.map { prefs ->
        val jsonString = prefs[USERS_JSON]
        val currentEmail = prefs[CURRENT_USER_EMAIL]
        if (jsonString != null && currentEmail != null) {
            val users = JSONObject(jsonString)
            users.optString(currentEmail, null)
        } else null
    }

    suspend fun getPasswordForUser(email: String): String? {
        val prefs = context.dataStore.data.map { it[USERS_JSON] }.firstOrNull()
        return prefs?.let {
            val users = JSONObject(it)
            users.optString(email, null)
        }
    }

    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs.remove(CURRENT_USER_EMAIL)
            // Opcional: podrías limpiar cosas asociadas al usuario actual aquí si quisieras
        }
    }

    // --- Carrito ---
    val getCartItems: Flow<List<String>> = context.dataStore.data.map { prefs ->
        val cartJson = prefs[CART_JSON]
        val currentUser = prefs[CURRENT_USER_EMAIL]

        if (cartJson != null && currentUser != null) {
            val allCarts = JSONObject(cartJson)
            val userCart = allCarts.optJSONArray(currentUser)
            userCart?.let {
                List(it.length()) { i -> it.getString(i) }
            } ?: emptyList()
        } else emptyList()
    }

    suspend fun addToCart(productName: String) {
        context.dataStore.edit { prefs ->
            val currentUser = prefs[CURRENT_USER_EMAIL] ?: return@edit
            val cartJson = prefs[CART_JSON]
            val allCarts = if (cartJson != null) JSONObject(cartJson) else JSONObject()

            val userCart = allCarts.optJSONArray(currentUser) ?: JSONArray()
            userCart.put(productName)
            allCarts.put(currentUser, userCart)

            prefs[CART_JSON] = allCarts.toString()
        }
    }

    // Eliminar solo una instancia del producto
    suspend fun removeFromCart(productName: String) {
        context.dataStore.edit { prefs ->
            val currentUser = prefs[CURRENT_USER_EMAIL] ?: return@edit
            val cartJson = prefs[CART_JSON]
            val allCarts = if (cartJson != null) JSONObject(cartJson) else JSONObject()

            val userCart = allCarts.optJSONArray(currentUser) ?: JSONArray()
            val newCart = JSONArray()
            var removed = false

            for (i in 0 until userCart.length()) {
                val item = userCart.optString(i)
                if (!removed && item == productName) {
                    removed = true
                } else {
                    newCart.put(item)
                }
            }

            allCarts.put(currentUser, newCart)
            prefs[CART_JSON] = allCarts.toString()
        }
    }

    suspend fun clearCart() {
        context.dataStore.edit { prefs ->
            val currentUser = prefs[CURRENT_USER_EMAIL] ?: return@edit
            val cartJson = prefs[CART_JSON]
            val allCarts = if (cartJson != null) JSONObject(cartJson) else JSONObject()
            allCarts.put(currentUser, JSONArray())
            prefs[CART_JSON] = allCarts.toString()
        }
    }

    // --- Historial de compras ---
    // Lista de nombres de productos comprados por el usuario actual
    val purchaseHistory: Flow<List<String>> = context.dataStore.data.map { prefs ->
        val historyJson = prefs[PURCHASE_HISTORY_JSON]
        val currentUser = prefs[CURRENT_USER_EMAIL]

        if (historyJson != null && currentUser != null) {
            val allHistory = JSONObject(historyJson)
            val userHistory = allHistory.optJSONArray(currentUser)
            userHistory?.let {
                List(it.length()) { i -> it.getString(i) }
            } ?: emptyList()
        } else emptyList()
    }

    suspend fun addPurchaseToHistory(productNames: List<String>) {
        context.dataStore.edit { prefs ->
            val currentUser = prefs[CURRENT_USER_EMAIL] ?: return@edit
            val historyJson = prefs[PURCHASE_HISTORY_JSON]
            val allHistory = if (historyJson != null) JSONObject(historyJson) else JSONObject()

            val userHistory = allHistory.optJSONArray(currentUser) ?: JSONArray()

            productNames.forEach { name ->
                userHistory.put(name)
            }

            allHistory.put(currentUser, userHistory)
            prefs[PURCHASE_HISTORY_JSON] = allHistory.toString()
        }
    }

    // --- Foto de perfil ---
    val profilePictureUri: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[PROFILE_PICTURE_URI]
    }

    suspend fun saveProfilePicture(uri: String) {
        context.dataStore.edit { prefs ->
            prefs[PROFILE_PICTURE_URI] = uri
        }
    }
}