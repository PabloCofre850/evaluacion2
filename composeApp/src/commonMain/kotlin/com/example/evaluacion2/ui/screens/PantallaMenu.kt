package com.example.evaluacion2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val CGEBlue      = Color(0xFF4A148C)

@Composable
fun PantallaMenu(
    onClientes: () -> Unit, // nav.goTo(Screen.Clientes)
    onMedidores: () -> Unit,
    onLecturas: () -> Unit,
    onBoletas: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)                                   // fondo blanco
            .border(10.dp, CGEBlue)   // marco azul redondeado
            .padding(20.dp)                                            // espacio interior
    )
    Row( // Fila principal
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column( // Columna Bienvenido
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight(),  // Ocupa 2/3 del total de la fila
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("¡Bienvenido!", style = MaterialTheme.typography.displayLarge)
            Text("CGE", style = MaterialTheme.typography.displayLarge)
            Text("¡Tu empresa de confianza!", style = MaterialTheme.typography.displayLarge)
        }

        Spacer(Modifier.width(40.dp))

        // Columna Menu

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight() // Ocupa 1/3 del total de la fila
                .border(8.dp, CGEBlue, RoundedCornerShape(10.dp))
                .padding(16.dp),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Panel de gestion",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = CGEBlue
                ))

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = onClientes,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(280.dp)
                    .height(80.dp)
            ) { Text("Gestionar Clientes") }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onMedidores,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(280.dp)
                    .height(80.dp)
            ) { Text("Gestionar Medidores") }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onLecturas,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(280.dp)
                    .height(80.dp)
            ) { Text("Gestionar Lecturas") }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onBoletas,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(280.dp)
                    .height(80.dp)
            ) { Text("Gestionar Boletas") }
        }
    }
}
