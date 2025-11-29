package com.example.ejemplonavycomp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ejemplonavycomp.data.Category
import com.example.ejemplonavycomp.viewmodel.CategoryUiState
import com.example.ejemplonavycomp.viewmodel.CategoryViewModel
import com.example.ejemplonavycomp.viewmodel.RegistroViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Producto1Scr(
    navCtrl: NavHostController,
    productId: String
) {
    val context = LocalContext.current
    val registroViewModel = remember { RegistroViewModel(context) }

    // ViewModel que consume el Gist
    val categoryViewModel: CategoryViewModel = viewModel()
    val uiState by categoryViewModel.uiState.collectAsState()

    // Para lanzar corrutinas desde onClick
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { AppTopBar(navCtrl) }
    ) { paddingValues ->

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
                    // Buscar el producto por id
                    val category = state.response.categories.find { it.id == productId }

                    if (category == null) {
                        Text(
                            text = "Producto no encontrado",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        ProductoDetailContent(
                            category = category,
                            onAddToCart = { name ->
                                scope.launch {
                                    registroViewModel.addToCart(name)
                                    navCtrl.navigate("texto")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoDetailContent(
    category: Category,
    onAddToCart: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Imagen
        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = category.thumbnail,
                contentDescription = category.name ?: "Producto",
                modifier = Modifier.size(180.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = category.name ?: "Producto",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = category.description ?: "Sin descripción",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Precio: ${category.price ?: "Sin precio"}",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                onAddToCart(category.name ?: "Producto")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(50)
        ) {
            Text(
                text = "Añadir al carrito",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
    }
}