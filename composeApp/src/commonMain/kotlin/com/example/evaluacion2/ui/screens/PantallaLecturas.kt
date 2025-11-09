package com.example.evaluacion2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evaluacion2.shared.dominio.LecturaConsumo
import com.example.evaluacion2.shared.persistencia.PersistenciaDatos
import com.example.evaluacion2.shared.persistencia.StorageDriver
import com.example.evaluacion2.shared.persistencia.LecturaRepoImpl

private val CGEBlue      = Color(0xFF4A148C)

@Composable
fun PantallaLecturas(
    onVolver: () -> Unit
) {
    val repo = remember { LecturaRepoImpl(PersistenciaDatos(StorageDriver())) }

    // --------- FILTRO O CONSULTA DE LECTURAS -------------
    var filtroMedidor by remember { mutableStateOf("") }
    var filtroAnio    by remember { mutableStateOf("") }
    var filtroMes     by remember { mutableStateOf("") }
    var lecturas      by remember { mutableStateOf(listOf<LecturaConsumo>()) }
    var mostrandoFormulario by remember { mutableStateOf(false) }

    // recarga cada vez que cambian los filtros
    LaunchedEffect(filtroMedidor, filtroAnio, filtroMes) {
        val a = filtroAnio.toIntOrNull() ?: 0
        val m = filtroMes.toIntOrNull() ?: 0
        lecturas = if (filtroMedidor.isNotBlank() && a > 0 && m in 1..12) {
            repo.listarPorMedidorMes(filtroMedidor, a, m)
        } else {
            emptyList()
        }
    }

    // --------- ESTADOS DEL FORMULARIO
    var formIdMedidor by remember { mutableStateOf("") }
    var formAnio      by remember { mutableStateOf("") }
    var formMes       by remember { mutableStateOf("") }
    var formKwh       by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CGEBlue)  // color de la “barra”
            .padding(vertical = 8.dp, horizontal = 100.dp)            // espacio dentro
    ){
        Text("Registrar / Ver lecturas", fontSize = 40.sp, color = Color.White, fontWeight = FontWeight.Bold)

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Spacer(Modifier.height(52.dp))

        // Campos de filtrado
        OutlinedTextField(
            value = filtroMedidor,
            onValueChange = { filtroMedidor = it },
            label = { Text("ID (Codigo) del Medidor") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        Row {
            OutlinedTextField(
                value = filtroAnio,
                onValueChange = { filtroAnio = it },
                label = { Text("Año (Ejemplo 2025)") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            OutlinedTextField(
                value = filtroMes,
                onValueChange = { filtroMes = it },
                label = { Text("Mes") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            // Al abrir el diálogo inicializa sus campos desde los filtros
            formIdMedidor = filtroMedidor
            formAnio      = filtroAnio
            formMes       = filtroMes
            formKwh       = ""
            mostrandoFormulario = true
        }) {
            Text("Registrar nueva lectura")
        }

        Spacer(Modifier.height(16.dp))

        // ——— Tabla de lecturas —————————————————————————————————————
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
                .padding(8.dp)
        ) {
            item {
                Row(Modifier.fillMaxWidth()) {
                    Text("ID Medidor", Modifier.weight(1f), style = MaterialTheme.typography.labelLarge)
                    Text("Año",        Modifier.weight(1f), style = MaterialTheme.typography.labelLarge)
                    Text("Mes",        Modifier.weight(1f), style = MaterialTheme.typography.labelLarge)
                    Text("kWh",        Modifier.weight(1f), style = MaterialTheme.typography.labelLarge)
                }
                Divider()
            }
            items(lecturas) { l ->
                Row(Modifier.fillMaxWidth()) {
                    Text(l.idMedidor,       Modifier.weight(1f))
                    Text(l.anio.toString(), Modifier.weight(1f))
                    Text(l.mes.toString(),  Modifier.weight(1f))
                    Text(l.kwhLeidos.toString(), Modifier.weight(1f))
                }
                Divider()
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = onVolver) { Text("Volver al menú") }
    }

    // ─── DIÁLOGO: Nuevo registro de lectura ───────────────────────────────────────
    if (mostrandoFormulario) {
        AlertDialog(
            onDismissRequest = { mostrandoFormulario = false },
            title = { Text("Nueva Lectura") },
            text = {
                Column {
                    OutlinedTextField(
                        value = formIdMedidor,
                        onValueChange = { formIdMedidor = it },
                        label = { Text("ID Medidor") },
                        singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = formAnio,
                        onValueChange = { formAnio = it },
                        label = { Text("Año") },
                        singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = formMes,
                        onValueChange = { formMes = it },
                        label = { Text("Mes") },
                        singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = formKwh,
                        onValueChange = { formKwh = it },
                        label = { Text("kWh Leídos") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val a = formAnio.toIntOrNull()
                    val m = formMes.toIntOrNull()
                    val k = formKwh.toDoubleOrNull()
                    if (formIdMedidor.isNotBlank() && a != null && m != null && k != null) {
                        repo.registrar(LecturaConsumo(formIdMedidor, a, m, k))
                        mostrandoFormulario = false
                    }
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                Button(onClick = { mostrandoFormulario = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

