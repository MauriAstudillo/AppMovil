package com.example.ejemplonavycomp.screens


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.ejemplonavycomp.viewmodel.RegistroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(navCtrl: NavHostController) {
    val context = LocalContext.current
    val viewModel = remember { RegistroViewModel(context) }

    // Observamos si el usuario está logeado
    val currentUser by viewModel.currentUserEmail.collectAsState(initial = null)
    val isLoggedIn = currentUser != null && currentUser!!.isNotEmpty()

    TopAppBar(
        title = {
            Text(
                text = "Tienda LEVELUP",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { navCtrl.navigate("home") }
            )
        },
        actions = {
            IconButton(onClick = { navCtrl.navigate("texto") }) {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = "Carrito"
                )
            }

            IconButton(onClick = {
                if (isLoggedIn) {
                    navCtrl.navigate("perfil")
                } else {
                    navCtrl.navigate("login")
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = if (isLoggedIn) "Perfil" else "Login"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        )
    )
}