package com.example.ejemplonavycomp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconsScr() {
    Scaffold(
        /*ELEMENTOS SCAFOLD*/
        topBar = {
            TopAppBar(
                title={
                    Text(text="Componente de Iconos")
                },
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary

                )
            )
        }
    )
    {paddingValues ->
        /*CONTENIDO*/
        Column(
            modifier = Modifier.padding(paddingValues)
        )
        {

        }
    }
}