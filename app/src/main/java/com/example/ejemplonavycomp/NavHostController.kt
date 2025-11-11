package com.example.ejemplonavycomp

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ejemplonavycomp.screens.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavHostController() {
    val navCtrl = rememberNavController()

    NavHost(
        navController = navCtrl,
        startDestination = "home",

        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(500)
            ) + fadeIn(animationSpec = tween(600))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(500)
            ) + fadeOut(animationSpec = tween(600))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(500)
            ) + fadeIn(animationSpec = tween(600))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(500)
            ) + fadeOut(animationSpec = tween(600))
        }
    ) {
        composable("home") { HomeScr(navCtrl) }
        composable("texto") { TextScr(navCtrl) }
        composable("login") { LoginScr(navCtrl) }
        composable("producto1") { Producto1Scr(navCtrl) }
        composable("perfil") { PerfilScr(navCtrl) }
        composable("registro") { RegistroScr(navCtrl) }
        composable("producto2"){Producto2Scr(navCtrl)}
    }
}

