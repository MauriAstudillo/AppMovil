package com.example.ejemplonavycomp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ejemplonavycomp.screens.HomeScr
import com.example.ejemplonavycomp.screens.LoginScr
import com.example.ejemplonavycomp.screens.PerfilScr
import com.example.ejemplonavycomp.screens.Producto1Scr
import com.example.ejemplonavycomp.screens.RegistroScr
import com.example.ejemplonavycomp.screens.TextScr

@Composable
fun NavHostController() {

    val navCtrl = rememberNavController()

    NavHost(
        navController = navCtrl,
        startDestination = "home"

    ){
        composable(route="home"){ HomeScr(navCtrl) }
        composable(route="texto"){ TextScr(navCtrl) }
        composable(route="login"){ LoginScr(navCtrl) }
        composable(route="producto1"){Producto1Scr(navCtrl)}
        composable(route="perfil"){PerfilScr(navCtrl)}
        composable(route="registro"){RegistroScr(navCtrl)}
    }

}

