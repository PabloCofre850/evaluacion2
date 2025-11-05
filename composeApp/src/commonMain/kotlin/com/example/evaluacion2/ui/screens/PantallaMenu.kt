package com.example.evaluacion2.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp

@Composable
fun PantallaMenu() {
    var abrirClientes by remember { mutableStateOf(false) }
    var abrirMedidores by remember { mutableStateOf(false) }
    var abrirLecturas by remember { mutableStateOf(false) }
    var abrirBoletas by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("¡Bienvenido!", style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(Modifier.width(40.dp))

        Column(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { abrirClientes = true },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.width(220.dp).height(50.dp)
            ) { Text("Gestionar Clientes") }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { abrirMedidores = true },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.width(220.dp).height(50.dp)
            ) { Text("Gestionar Medidores") }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { abrirLecturas = true },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.width(220.dp).height(50.dp)
            ) { Text("Gestionar Lecturas") }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { abrirBoletas = true },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.width(220.dp).height(50.dp)
            ) { Text("Gestionar Boletas") }
        }
    }

    // En commonMain, usa diálogos o navegación interna en lugar de múltiples Window
    if (abrirClientes) {
        DialogPantallaSimple(
            titulo = "Gestión de Clientes",
            onClose = { abrirClientes = false }
        ) { PantallaClientes() }
    }
    if (abrirMedidores) {
        DialogPantallaSimple(
            titulo = "Gestión de Medidores",
            onClose = { abrirMedidores = false }
        ) { PantallaMedidores() }
    }
    if (abrirLecturas) {
        DialogPantallaSimple(
            titulo = "Gestión de Lecturas",
            onClose = { abrirLecturas = false }
        ) { PantallaLecturas() }
    }
    if (abrirBoletas) {
        DialogPantallaSimple(
            titulo = "Gestión de Boletas",
            onClose = { abrirBoletas = false }
        ) { PantallaBoletas() }
    }
}

@Composable
private fun DialogPantallaSimple(
    titulo: String,
    onClose: () -> Unit,
    contenido: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text(titulo) },
        text = { contenido() },
        confirmButton = {
            TextButton(onClick = onClose) { Text("Cerrar") }
        }
    )
}

// Pantallas simples compartidas
@Composable fun PantallaClientes() { Text("Pantalla de Clientes") }
@Composable fun PantallaMedidores() { Text("Pantalla de Medidores") }
@Composable fun PantallaLecturas() { Text("Pantalla de Lecturas") }
@Composable fun PantallaBoletas() { Text("Pantalla de Boletas") }
