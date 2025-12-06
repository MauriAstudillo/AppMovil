package com.example.ejemplonavycomp.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ejemplonavycomp.data.UserPreferences
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RegistroViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    // Mocks que necesitamos
    private lateinit var mockContext: Context
    private lateinit var mockUserPreferences: UserPreferences

    // El ViewModel que vamos a probar
    private lateinit var viewModel: RegistroViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // Preparamos los mocks
        mockContext = mockk(relaxed = true)
        mockUserPreferences = mockk(relaxed = true)

        // Creamos una instancia REAL del ViewModel
        viewModel = RegistroViewModel(mockContext)

        // Usamos reflexión para reemplazar el `prefs` interno por nuestro mock
        try {
            val prefsField = viewModel.javaClass.getDeclaredField("prefs")
            prefsField.isAccessible = true // Hacemos el campo privado accesible
            prefsField.set(viewModel, mockUserPreferences) // Reemplazamos el objeto
        } catch (e: NoSuchFieldException) {
            throw(AssertionError("El campo 'prefs' no se encontró en RegistroViewModel. ¿Cambiaste el nombre de la variable?"))
        }
    }

    // --- Pruebas de Registro / Login ---

    @Test
    fun `cuando saveUser es llamado, debe invocar a prefs_saveUser`() = runTest {
        // Arrange
        val email = "test@example.com"
        val password = "password123"
        coEvery { mockUserPreferences.saveUser(email, password) } returns Unit

        // Act
        viewModel.saveUser(email, password)

        // Verify
        coVerify(exactly = 1) { mockUserPreferences.saveUser(email, password) }
    }

    @Test
    fun `cuando logout es llamado, debe invocar a prefs_logout`() = runTest {
        // Arrange
        coEvery { mockUserPreferences.logout() } returns Unit

        // Act
        viewModel.logout()

        // Verify
        coVerify(exactly = 1) { mockUserPreferences.logout() }
    }

    @Test
    fun `cuando setCurrentUser es llamado, debe invocar a prefs_setCurrentUser`() = runTest {
        // Arrange
        val email = "currentUser@example.com"
        coEvery { mockUserPreferences.setCurrentUser(email) } returns Unit

        // Act
        viewModel.setCurrentUser(email)

        // Verify
        coVerify(exactly = 1) { mockUserPreferences.setCurrentUser(email) }
    }

    @Test
    fun `getPasswordForUser debe devolver la password correcta del mock`() = runTest {
        // Arrange
        val email = "user@test.com"
        val expectedPassword = "password-from-mock"
        coEvery { mockUserPreferences.getPasswordForUser(email) } returns expectedPassword

        // Act
        val actualPassword = viewModel.getPasswordForUser(email)

        // Assert: Comprobamos que el valor devuelto es el esperado
        assertEquals(expectedPassword, actualPassword)
    }

    // foto

    @Test
    fun `cuando updateProfilePicture es llamado, debe invocar a prefs_saveProfilePicture`() = runTest {
        // Arrange
        val uri = "content://pictures/my-profile-pic.jpg"
        coEvery { mockUserPreferences.saveProfilePicture(uri) } returns Unit

        // Act
        viewModel.updateProfilePicture(uri)

        // Verify
        coVerify(exactly = 1) { mockUserPreferences.saveProfilePicture(uri) }
    }


    // carrito

    @Test
    fun `cuando addToCart es llamado, debe invocar a prefs_addToCart`() = runTest {
        // Arrange
        val productName = "Laptop Pro"
        coEvery { mockUserPreferences.addToCart(productName) } returns Unit

        // Act
        viewModel.addToCart(productName)

        // Verify
        coVerify(exactly = 1) { mockUserPreferences.addToCart(productName) }
    }

    @Test
    fun `cuando removeFromCart es llamado, debe invocar a prefs_removeFromCart`() = runTest {
        // Arrange
        val productName = "Teclado Mecánico"
        coEvery { mockUserPreferences.removeFromCart(productName) } returns Unit

        // Act
        viewModel.removeFromCart(productName)

        // Verify
        coVerify(exactly = 1) { mockUserPreferences.removeFromCart(productName) }
    }

    @Test
    fun `cuando clearCart es llamado, debe invocar a prefs_clearCart`() = runTest {
        // Arrange
        coEvery { mockUserPreferences.clearCart() } returns Unit

        // Act
        viewModel.clearCart()

        // Verify
        coVerify(exactly = 1) { mockUserPreferences.clearCart() }
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}



