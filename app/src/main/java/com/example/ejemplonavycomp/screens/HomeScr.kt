package com.example.ejemplonavycomp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScr(navCtrl: NavHostController) {
    Scaffold(
        /*ELEMENTOS SCAFOLD*/
        topBar = { AppTopBar(navCtrl) }
    )
    {paddingValues ->
        /*CONTENIDO*/
        Column(
            modifier = Modifier.padding(paddingValues)
        )
        {
            Row(
                Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    //.background(color = Color.Red)

            )
            {
                Text(text="Listado de Productos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)
            }
            HorizontalDivider()
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically


            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    modifier = Modifier
                        .padding(25.dp)
                        .size(width = 150.dp, height = 150.dp)
                        .clickable{
                            navCtrl.navigate(route="producto1")
                        }
                ) {
                    //tengo que añadir imagen xddd
                    Box(
                        modifier = Modifier.padding(horizontal = 60.dp, vertical = 16.dp)
                    ) {
                        Icon(Icons.Default.Keyboard, contentDescription = "Check mark")
                    }
                    Text(
                        text = "Teclado Gamer",
                        modifier = Modifier
                            .padding(horizontal=35.dp, vertical = 16.dp ),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$25.990",
                        modifier = Modifier.padding(horizontal = 50.dp, vertical = 5.dp)
                    )

                }
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    modifier = Modifier
                        .size(width = 150.dp, height = 150.dp)
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 60.dp, vertical = 16.dp)
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = "Check mark")
                    }
                    Text(
                        text = "Producto 2",
                        modifier = Modifier
                            .padding(horizontal=35.dp, vertical = 16.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Precio",
                        modifier = Modifier.padding(horizontal = 50.dp, vertical = 5.dp)
                    )
                }
            }
            HorizontalDivider()
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically


            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    modifier = Modifier
                        .padding(25.dp)
                        .size(width = 150.dp, height = 150.dp)
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 60.dp, vertical = 16.dp)
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = "Check mark")
                    }
                    Text(
                        text = "Producto 3",
                        modifier = Modifier
                            .padding(horizontal=35.dp, vertical = 16.dp ),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Precio",
                        modifier = Modifier.padding(horizontal = 50.dp, vertical = 5.dp)
                    )

                }
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    modifier = Modifier
                        .size(width = 150.dp, height = 150.dp)
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 60.dp, vertical = 16.dp)
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = "Check mark")
                    }
                    Text(
                        text = "Producto 4",
                        modifier = Modifier
                            .padding(horizontal=35.dp, vertical = 16.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Precio",
                        modifier = Modifier.padding(horizontal = 50.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }

}

