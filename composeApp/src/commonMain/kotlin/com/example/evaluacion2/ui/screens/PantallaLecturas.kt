package com.example.evaluacion2.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.evaluacion2.shared.dominio.LecturaConsumo
import com.example.evaluacion2.shared.persistencia.PersistenciaDatos
import com.example.evaluacion2.shared.persistencia.StorageDriver
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.evaluacion2.shared.persistencia.LecturaRepoImpl
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider

@Composable
fun PantallaLecturas(
    onVolver: () -> Unit
) {

    val repo = remember { LecturaRepoImpl(PersistenciaDatos(StorageDriver())) }

    var idMedidor by remember { mutableStateOf("") }

    var anio by remember { mutableStateOf("") }

    var mes by remember { mutableStateOf("") }

    var kwh by remember { mutableStateOf("") }

    var mostrandoFormulario by remember { mutableStateOf(false) }

    var lecturas by remember { mutableStateOf(listOf<LecturaConsumo>()) }

    LaunchedEffect(idMedidor, anio, mes, mostrandoFormulario) {
        val a = anio.toIntOrNull() ?: 0

        val m = mes.toIntOrNull() ?: 0

        if (idMedidor.isNotBlank() && a > 0 && m in 1..12) {
            lecturas = repo.listarPorMedidorMes(idMedidor, a, m)
        } else {
            lecturas = emptyList()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text("Registrar / Ver lecturas", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = idMedidor,
            onValueChange = { idMedidor = it },
            label = { Text("ID Medidor") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.5f)
        )

        Spacer(Modifier.height(8.dp))

        Row {
            OutlinedTextField(
                value = anio,
                onValueChange = { anio = it },
                label = { Text("Año") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(1f)
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = mes,
                onValueChange = { mes = it },
                label = { Text("Mes") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(1f)
            )
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = { mostrandoFormulario = true }) {
            Text("Registrar nueva lectura")
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
                .padding(8.dp)
        ) {
            item {
                Row(Modifier.fillMaxWidth()) {
                    Text("ID Medidor", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelLarge)
                    Text("Año", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelLarge)
                    Text("Mes", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelLarge)
                    Text("kWh", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelLarge)
                }
                Divider()
            }
            items(lecturas) {
                l ->
                Row(Modifier.fillMaxWidth()) {
                    Text(l.idMedidor, modifier = Modifier.weight(1f))
                    Text(l.anio.toString(), modifier = Modifier.weight(1f))
                    Text(l.mes.toString(), modifier = Modifier.weight(1f))
                    Text(l.kwhLeidos.toString(), modifier = Modifier.weight(1f))
                }
                Divider()

            }
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = { onVolver() }) {
            Text("Volver al menu")
        }
    }

    if (mostrandoFormulario) {
        AlertDialog(
            onDismissRequest = { mostrandoFormulario = false },
            title = { Text("Nueva Lectura") },
            text = {
                Column {
                    OutlinedTextField(
                        value = idMedidor,
                        onValueChange = { idMedidor = it },
                        label = { Text("ID Medidor") },
                        singleLine = true
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = anio,
                        onValueChange = { anio = it },
                        label = { Text("Año")},
                        singleLine = true
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = mes,
                        onValueChange = { mes = it },
                        label = { Text("Mes") },
                        singleLine = true
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = kwh,
                        onValueChange = { kwh = it },
                        label = { Text("Kwh Leidos") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val a = anio.toIntOrNull()
                    val m = mes.toIntOrNull()
                    val k = kwh.toDoubleOrNull()

                    if (idMedidor.isNotBlank() && a != null && m != null && k != null) {
                        repo.registrar(LecturaConsumo(idMedidor, a, m, k))
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

@org.jetbrains.compose.ui.tooling.preview.Preview
@Composable
private fun PantallaLecturasPreview() {
    PantallaLecturas(onVolver = {})
}
