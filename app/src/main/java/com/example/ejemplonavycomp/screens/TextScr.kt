package com.example.ejemplonavycomp.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ejemplonavycomp.viewmodel.CategoryUiState
import com.example.ejemplonavycomp.viewmodel.CategoryViewModel
import com.example.ejemplonavycomp.viewmodel.RegistroViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextScr(navCtrl: NavHostController) {
    val context = LocalContext.current
    val registroVM = remember { RegistroViewModel(context) }
    val categoryVM: CategoryViewModel = viewModel()

    val scope = rememberCoroutineScope()

    val productos by registroVM.cartItems.collectAsState(initial = emptyList())
    val currentUser by registroVM.currentUserEmail.collectAsState(initial = null)
    val uiState by categoryVM.uiState.collectAsState()

    var compraRealizada by remember { mutableStateOf(false) }

    /* ---------------------------
       1. USUARIO NO LOGEADO
       --------------------------- */
    if (currentUser.isNullOrEmpty()) {
        Scaffold(topBar = { AppTopBar(navCtrl) }) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Debes iniciar sesión para ver tu carrito",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { navCtrl.navigate("login") },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .width(200.dp)
                            .height(50.dp)
                    ) {
                        Text("Iniciar sesión")
                    }
                }
            }
        }
        return
    }

    /* ---------------------------
       2. PANTALLA PRINCIPAL DEL CARRITO
       --------------------------- */

    Scaffold(topBar = { AppTopBar(navCtrl) }) { paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            when (val state = uiState) {

                /* ----- Loading ------ */
                is CategoryUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                /* ----- Error ------ */
                is CategoryUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                /* ----- Success: mostrar productos del gist ------ */
                is CategoryUiState.Success -> {

                    // Convertir los nombres guardados en DataStore a categorías completas
                    val allCategories = state.response.categories

                    val cartCategories = productos.mapNotNull { name ->
                        allCategories.find { it.name == name }
                    }

                    if (cartCategories.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (compraRealizada) "Compra realizada"
                                else "Tu carrito está vacío",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        Column(modifier = Modifier.fillMaxSize()) {

                            LazyColumn(
                                modifier = Modifier.weight(1f)
                            ) {
                                itemsIndexed(
                                    cartCategories,
                                    key = { index, item -> "${item.id}-$index" }
                                ) { index, category ->

                                    var visible by remember { mutableStateOf(true) }

                                    AnimatedVisibility(
                                        visible = visible,
                                        exit = fadeOut(animationSpec = tween(300)) +
                                                slideOutHorizontally { -300 }
                                    ) {

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {

                                            Row(verticalAlignment = Alignment.CenterVertically) {

                                                AsyncImage(
                                                    model = category.thumbnail,
                                                    contentDescription = category.name,
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier
                                                        .size(45.dp)
                                                        .clip(RoundedCornerShape(8.dp))
                                                )

                                                Spacer(modifier = Modifier.width(16.dp))

                                                Column {
                                                    Text(
                                                        text = category.name,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(
                                                        text = category.price,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                            }

                                            IconButton(onClick = {
                                                scope.launch {
                                                    visible = false
                                                    delay(300)
                                                    registroVM.removeFromCart(category.name)
                                                }
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "Eliminar",
                                                    tint = Color.Red
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            /* FINALIZAR COMPRA */
                            Button(
                                onClick = {
                                    scope.launch {
                                        registroVM.clearCart()
                                        compraRealizada = true
                                        delay(2000)
                                        compraRealizada = false
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .height(55.dp),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = "Finalizar compra",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}