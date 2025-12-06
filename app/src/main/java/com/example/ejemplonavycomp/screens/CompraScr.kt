package com.example.ejemplonavycomp.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.ejemplonavycomp.viewmodel.CategoryUiState
import com.example.ejemplonavycomp.viewmodel.CategoryViewModel
import com.example.ejemplonavycomp.viewmodel.RegistroViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import java.text.NumberFormat
import java.util.Locale
import com.example.ejemplonavycomp.data.Category

private fun parsePriceToInt(price: String): Int {
    val digits = price.filter { it.isDigit() }
    return digits.toIntOrNull() ?: 0
}

private fun formatPriceCL(value: Int): String {
    val nf = NumberFormat.getNumberInstance(Locale("es", "CL"))
    return "$" + nf.format(value)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompraScr(navCtrl: NavHostController) {
    val context = LocalContext.current
    val registroVM = remember { RegistroViewModel(context) }
    val categoryVM: CategoryViewModel = viewModel()

    val scope = rememberCoroutineScope()

    val productos by registroVM.cartItems.collectAsState(initial = emptyList())
    val currentUser by registroVM.currentUserEmail.collectAsState(initial = null)
    val uiState by categoryVM.uiState.collectAsState()
    var compraRealizada by remember { mutableStateOf(false) }
    var resumenCompra by remember { mutableStateOf<List<Category>>(emptyList()) }

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
                        text = "Debes iniciar sesión para continuar con la compra",
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

    Scaffold(topBar = { AppTopBar(navCtrl) }) { paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            when (val state = uiState) {

                is CategoryUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is CategoryUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is CategoryUiState.Success -> {

                    val allCategories = state.response.categories

                    val cartCategories = productos.mapNotNull { name ->
                        allCategories.find { it.name == name }
                    }

                    val totalInt = cartCategories.sumOf { parsePriceToInt(it.price) }
                    val totalFormatted = formatPriceCL(totalInt)

                    if (cartCategories.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Tu carrito está vacío",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {

                        var nombreTarjeta by remember { mutableStateOf("") }
                        var numeroTarjeta by remember { mutableStateOf("") }
                        var fechaExp by remember { mutableStateOf("") }
                        var cvv by remember { mutableStateOf("") }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {

                            Text(
                                text = "Resumen de compra",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            LazyColumn(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
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
                                                .padding(vertical = 8.dp),
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

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Total: $totalFormatted",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Datos de tarjeta",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = nombreTarjeta,
                                onValueChange = { nombreTarjeta = it },
                                label = { Text("Nombre en la tarjeta") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = numeroTarjeta,
                                onValueChange = { numeroTarjeta = it },
                                label = { Text("Número de tarjeta") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = fechaExp,
                                    onValueChange = { fechaExp = it },
                                    label = { Text("MM/AA") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )

                                OutlinedTextField(
                                    value = cvv,
                                    onValueChange = { cvv = it },
                                    label = { Text("CVV") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    scope.launch {
                                        registroVM.addPurchaseToHistory(
                                            cartCategories.map { it.name }
                                        )
                                        // Guardamos los productos actuales para mostrar en la animación
                                        resumenCompra = cartCategories.toList()

                                        // Limpiar carrito
                                        registroVM.clearCart()

                                        // Mostrar animación de compra realizada
                                        compraRealizada = true

                                        // Esperar un momento y volver al home
                                        delay(2500)
                                        compraRealizada = false
                                        navCtrl.navigate("home") {
                                            popUpTo("home") { inclusive = true }
                                        }
                                    }
                                },
                                enabled = totalInt > 0,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(55.dp),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = "Pagar",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = compraRealizada,
                enter = fadeIn(animationSpec = tween(300)) + scaleIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300)) + scaleOut(animationSpec = tween(300))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Compra realizada",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(72.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "¡Compra realizada!",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Has comprado:",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 160.dp)
                            ) {
                                items(resumenCompra) { category ->
                                    Text(
                                        text = "• ${category.name}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Serás redirigido al inicio...",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}