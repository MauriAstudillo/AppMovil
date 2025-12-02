package com.example.ejemplonavycomp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ejemplonavycomp.ui.theme.EjemploNavyCompTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EjemploNavyCompTheme(
                darkTheme = true,
                dynamicColor = false
            ) {
                NavHostController()
            }
        }
    }
}
