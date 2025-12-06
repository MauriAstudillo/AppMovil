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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.ejemplonavycomp.ui.theme.Azulelectrico
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
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { navCtrl.navigate("login") },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .width(220.dp)
                            .height(55.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Azulelectrico,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "Iniciar sesión",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
        return
    }

    Scaffold(topBar = { AppTopBar(navCtrl) }) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            // 🔹 ESTE BOTÓN SIEMPRE SE VE
            TextButton(
                onClick = { navCtrl.navigate("home") },
                modifier = Modifier
                    .padding(start = 12.dp, top = 4.dp)
            ) {
                Text(
                    text = "← Volver a la tienda",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Box(
                modifier = Modifier
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
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    is CategoryUiState.Success -> {

                        val allCategories = state.response.categories

                        val cartCategories = productos.mapNotNull { name ->
                            allCategories.find { it.name == name }
                        }

                        if (cartCategories.isEmpty()) {
                            // 🔹 Ahora el botón seguirá visible arriba
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Tu carrito está vacío",
                                    style = MaterialTheme.typography.titleLarge
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
                                                            .size(55.dp)
                                                            .clip(RoundedCornerShape(10.dp))
                                                    )

                                                    Spacer(modifier = Modifier.width(16.dp))

                                                    Column {
                                                        Text(
                                                            text = category.name,
                                                            style = MaterialTheme.typography.titleLarge,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                        Text(
                                                            text = category.price,
                                                            style = MaterialTheme.typography.titleMedium,
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

                                Button(
                                    onClick = {
                                        navCtrl.navigate("compra")
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                        .height(55.dp),
                                    shape = RoundedCornerShape(50),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Azulelectrico,
                                        contentColor = Color.Black
                                    )
                                ) {
                                    Text(
                                        text = "Finalizar compra",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}