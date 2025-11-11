package com.example.ejemplonavycomp.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ejemplonavycomp.viewmodel.RegistroViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextScr(navCtrl: NavHostController) {
    val context = LocalContext.current
    val viewModel = remember { RegistroViewModel(context) }
    val scope = rememberCoroutineScope()

    val productos by viewModel.cartItems.collectAsState(initial = emptyList())
    val currentUser by viewModel.currentUserEmail.collectAsState(initial = null)
    var compraRealizada by remember { mutableStateOf(false) }

    // ✅ Si no hay usuario logeado, mostrar mensaje de inicio de sesión
    if (currentUser.isNullOrEmpty()) {
        Scaffold(
            topBar = { AppTopBar(navCtrl) }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Debes iniciar sesión para ver tu carrito",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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

    // 🛍️ Si el usuario está logeado, mostrar el carrito normal
    Scaffold(
        topBar = { AppTopBar(navCtrl) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (productos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (compraRealizada) "Compra realizada"
                        else "Tu carrito está vacío",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    itemsIndexed(
                        productos,
                        key = { index, item -> "$item-$index" }
                    ) { index, producto ->

                        var visible by remember { mutableStateOf(true) }

                        AnimatedVisibility(
                            visible = visible,
                            exit = fadeOut(animationSpec = tween(300)) +
                                    slideOutHorizontally(targetOffsetX = { -300 })
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.ShoppingCart,
                                        contentDescription = "Carrito",
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(text = producto)
                                }

                                IconButton(onClick = {
                                    scope.launch {
                                        visible = false
                                        delay(300)
                                        viewModel.removeFromCart(producto)
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

                Button(
                    onClick = {
                        scope.launch {
                            viewModel.clearCart()
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