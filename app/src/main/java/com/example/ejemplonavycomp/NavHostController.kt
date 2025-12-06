package com.example.ejemplonavycomp

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ejemplonavycomp.screens.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavHostController() {
    val navCtrl = rememberNavController()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // 👈 aquí va tu Negrofondo vía theme
    ) {
        NavHost(
            navController = navCtrl,
            startDestination = "home",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(500)
                )
            },

            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(500)
                )
            }
        ) {
            composable("home")    { HomeScr(navCtrl) }
            composable("texto")   { TextScr(navCtrl) }
            composable("login")   { LoginScr(navCtrl) }
            composable("perfil")  { PerfilScr(navCtrl) }
            composable("registro"){ RegistroScr(navCtrl) }
            composable("compra")  { CompraScr(navCtrl) }
            composable("historial") { HistorialScr(navCtrl)}

            composable(
                route = "producto/{productId}",
                arguments = listOf(
                    navArgument("productId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: return@composable
                Producto1Scr(navCtrl = navCtrl, productId = productId)
            }
        }
    }
}

