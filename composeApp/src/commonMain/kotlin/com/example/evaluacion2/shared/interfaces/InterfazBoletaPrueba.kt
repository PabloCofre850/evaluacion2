package com.example.evaluacion2.shared.vistas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.evaluacion2.shared.dominio.*
import com.example.evaluacion2.shared.persistencia.*

@Composable
fun BoletaTestScreen() {
    // --- Variables del formulario ---
    var rut by remember { mutableStateOf("") }
    var mes by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var consumo by remember { mutableStateOf("") }
    var tipoTarifa by remember { mutableStateOf("Residencial") }

    // --- Repositorio en memoria para probar ---
    val repo = remember { BoletaRepoImpl(PersistenciaDatos(StorageDriver())) }
    val lista by remember { mutableStateOf(mutableListOf<Boleta>()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Simulador de Boletas", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(20.dp))

        // Campos de entrada
        Campo(label = "RUT", value = rut) { rut = it }
        Campo(label = "Mes", value = mes) { mes = it }
        Campo(label = "Año", value = anio) { anio = it }
        Campo(label = "Consumo (kWh)", value = consumo) { consumo = it }

        Spacer(Modifier.height(10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Tipo: ")
            DropdownMenuDemo(selected = tipoTarifa, onChange = { tipoTarifa = it })
        }

        Spacer(Modifier.height(20.dp))

        Button(onClick = {
            // Crear y guardar la boleta
            val tarifa = if (tipoTarifa == "Residencial") TarifaResidencial() else TarifaComercial()
            val detalle = tarifa.calcular(consumo.toDouble())

            val boleta = Boleta(
                idCliente = rut,
                anio = anio.toInt(),
                mes = mes.toInt(),
                kwhTotal = consumo.toDouble(),
                detalle = detalle,
                estado = EstadoBoleta.PENDIENTE
            )

            repo.guardar(boleta)
            lista.add(boleta)

        }) {
            Text("Guardar Boleta")
        }

        Spacer(Modifier.height(30.dp))
        Text("Boletas guardadas:")

        // Mostrar las boletas guardadas
        Column {
            lista.forEach {
                Text("• ${it.idCliente} (${it.mes}/${it.anio}) - ${it.detalle.nombre}: $${it.detalle.total}")
            }
        }
    }
}

// -----------------------
// Componentes reutilizables
// -----------------------

@Composable
fun Campo(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    )
}

@Composable
fun DropdownMenuDemo(selected: String, onChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) { Text(selected) }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("Residencial") }, onClick = {
                onChange("Residencial"); expanded = false
            })
            DropdownMenuItem(text = { Text("Comercial") }, onClick = {
                onChange("Comercial"); expanded = false
            })
        }
    }
}
