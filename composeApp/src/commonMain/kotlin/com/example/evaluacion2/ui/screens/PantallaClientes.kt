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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaClientes(
    onVolver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding()
    ) {
        Text(
            text = "Pantalla de Clientes",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(16.dp))

        // TODO: contenido de la lista/formulario de clientes

        Spacer(Modifier.height(24.dp))
        Button(onClick = onVolver) {
            Text("Volver al menú")
        }
    }
}

@org.jetbrains.compose.ui.tooling.preview.Preview
@Composable
private fun PantallaClientesPreview() {
    PantallaClientes(onVolver = {})
}
