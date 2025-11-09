package com.example.evaluacion2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evaluacion2.shared.dominio.*
import com.example.evaluacion2.shared.persistencia.*
import com.example.evaluacion2.shared.servicios.PdfService
import kotlinx.coroutines.launch

private val CGEBlue      = Color(0xFF4A148C)


@Composable
fun PantallaBoletas(
    onVolver: () -> Unit
) {
    // ---- Estado general ----
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // ---- Variables del formulario ----
    var rut by remember { mutableStateOf("") }
    var mes by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var consumo by remember { mutableStateOf("") }
    var tipoTarifa by remember { mutableStateOf("Residencial") }

    // ---- Repositorio en memoria ----
    val repo = remember { BoletaRepoImpl(PersistenciaDatos(StorageDriver())) }
    var lista by remember { mutableStateOf(listOf<Boleta>()) }

    // Con Scaffold creamos la estructura general de esta pantalla
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CGEBlue)  // color de la “barra”
                .padding(vertical = 8.dp, horizontal = 100.dp)            // espacio dentro
        ){
            Text("Gestion de boletas", fontSize = 40.sp, color = Color.White, fontWeight = FontWeight.Bold)

        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .safeContentPadding()
                .padding(52.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(52.dp))

            Campo(label = "RUT", value = rut, onChange = { rut = it })
            Campo(label = "Mes", value = mes, onChange = { mes = it }, tipo = KeyboardType.Number)
            Campo(label = "Año", value = anio, onChange = { anio = it }, tipo = KeyboardType.Number)
            Campo(label = "Consumo (kWh)", value = consumo, onChange = { consumo = it }, tipo = KeyboardType.Number)

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Tipo: ")
                DropdownMenuTarifa(
                    selected = tipoTarifa,
                    onChange = { tipoTarifa = it }
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (rut.isNotBlank() && mes.isNotBlank() && anio.isNotBlank() && consumo.isNotBlank()) {
                        val tarifa = if (tipoTarifa == "Residencial") TarifaResidencial() else TarifaComercial()
                        val detalle = tarifa.calcular(consumo.toDouble())

                        val boleta = Boleta(
                            idCliente = rut,
                            anio = anio.toInt(),
                            mes = mes.toInt(),
                            kwhTotal = consumo.toDouble(),
                            detalle = detalle,
                            estado = EstadoBoleta.EMITIDA
                        )

                        repo.guardar(boleta)
                        lista = lista + boleta

                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("¡Boleta guardada correctamente!")
                        }
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("¡¡Complete todos los campos!!")
                        }
                    }
                },
                modifier = Modifier.width(220.dp)
            ) {
                Text("Guardar Boleta")
            }

            Spacer(Modifier.height(24.dp))

            Text("Boletas guardadas:", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(8.dp))

            // --- Botones de acción ---
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Button(
                    onClick = {
                        if (lista.isNotEmpty()) {
                            try {
                                val pdfService = PdfService()
                                val bytes = pdfService.generarBoletasPdf(lista, emptyMap())
                                val texto = bytes.decodeToString()

                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Boletas generadas:\n${texto.take(100)}..."
                                    )
                                }
                            } catch (e: Exception) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Error al generar texto: ${e.message}")
                                }
                            }
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("No hay boletas para exportar.")
                            }
                        }
                    },
                    modifier = Modifier.width(180.dp)
                ) {
                    Text("Generar PDF")
                }

                Button(onClick = onVolver, modifier = Modifier.width(180.dp)) {
                    Text("Volver al menú")
                }
            }

            Spacer(Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                lista.forEach {
                    Text("• ${it.idCliente} (${it.mes}/${it.anio}) - ${it.detalle.nombre}: $${it.detalle.total}")
                }
            }
        }
    }
}

// -----------------------
// Componentes reutilizables
// -----------------------

@Composable
private fun Campo(label: String, value: String, onChange: (String) -> Unit, tipo: KeyboardType = KeyboardType.Text) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        keyboardOptions = KeyboardOptions(keyboardType = tipo)
    )
}

@Composable
private fun DropdownMenuTarifa(
    selected: String,
    onChange: (String) -> Unit)
{
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text(selected)
        }

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

@org.jetbrains.compose.ui.tooling.preview.Preview
@Composable
private fun PantallaBoletasPreview() {
    PantallaBoletas(onVolver = {})
}
