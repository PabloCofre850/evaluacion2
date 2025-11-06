package com.example.evaluacion2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaMenu(
    onClientes: () -> Unit,
    onMedidores: () -> Unit,
    onLecturas: () -> Unit,
    onBoletas: () -> Unit
) {
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
            Text("CGE", style = MaterialTheme.typography.headlineSmall)
        }

        Spacer(Modifier.width(40.dp))

        Column( // Columna Menu
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(), // Ocupa 1/3 del total de la fila
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Panel de gestión", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = onClientes,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(220.dp)
                    .height(50.dp)
            ) { Text("Gestionar Clientes") }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onMedidores,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(220.dp)
                    .height(50.dp)
            ) { Text("Gestionar Medidores") }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onLecturas,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(220.dp)
                    .height(50.dp)
            ) { Text("Gestionar Lecturas") }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onBoletas,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(220.dp)
                    .height(50.dp)
            ) { Text("Gestionar Boletas") }
        }
    }
}
