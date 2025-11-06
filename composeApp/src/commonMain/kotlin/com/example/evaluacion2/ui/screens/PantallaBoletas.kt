package com.example.evaluacion2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.evaluacion2.shared.dominio.*
import com.example.evaluacion2.shared.persistencia.*
import kotlinx.coroutines.launch

@Composable
fun PantallaBoletas(
    onVolver: () -> Unit
    // Volver a la pantalla anterior
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
        // Le pasamos snackbarHostState, que controla los mensajes en pantalla
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .safeContentPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Titulo de la pantalla
            Text(
                text = "Gestión de Boletas",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(24.dp))

            // --- Campos de entrada ---
            // Pedimos los datos para generar la boleta

            Campo(label = "RUT", value = rut, onChange = { rut = it })
            Campo(label = "Mes", value = mes, onChange = { mes = it }, tipo = KeyboardType.Number)
            Campo(label = "Año", value = anio, onChange = { anio = it }, tipo = KeyboardType.Number)
            Campo(label = "Consumo (kWh)", value = consumo, onChange = { consumo = it }, tipo = KeyboardType.Number)

            Spacer(Modifier.height(8.dp))


            // Seleccionamos el tipo de tarifa

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Tipo: ")
                DropdownMenuTarifa(
                    selected = tipoTarifa,
                    onChange = { tipoTarifa = it })

                    // Actualiza el estado local
            }

            Spacer(Modifier.height(16.dp))

            // --- Boton principal ---
            Button(
                onClick = {
                    if (rut.isNotBlank() && mes.isNotBlank() && anio.isNotBlank() && consumo.isNotBlank()) {
                        // Validamos que no hayan campos en blanco
                        val tarifa = if (tipoTarifa == "Residencial") TarifaResidencial() else TarifaComercial()

                        // Instancia la tarifa adecuada, segun el tipo seleccionado

                        val detalle = tarifa.calcular(consumo.toDouble())

                        // Calcula el detalle (subtotal, cargos, iva, total) en base al consumo

                        val boleta = Boleta(

                            // Crea el objeto Boleta con todos los datos del formulario

                            idCliente = rut,
                            anio = anio.toInt(),
                            mes = mes.toInt(),
                            kwhTotal = consumo.toDouble(),
                            detalle = detalle,
                            estado = EstadoBoleta.EMITIDA
                        )

                        repo.guardar(boleta)
                        lista = lista + boleta

                        // Guardamos en persistencia (BOLETA), y creamos una nueva lista con las boletas guardadas

                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("¡Boleta guardada correctamente!")
                            // Muestra el mensaje de que se guardo la boleta correctamente
                        }
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("¡¡Complete todos los campos!!")
                            // Si algun campo esta en blanco, muestra el mensaje de advertencia
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

            // --- Lista de boletas guardadas ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                lista.forEach {
                    Text("• ${it.idCliente} (${it.mes}/${it.anio}) - ${it.detalle.nombre}: $${it.detalle.total}")
                }
            }

            Spacer(Modifier.height(32.dp))
            Button(onClick = onVolver) {
                Text("Volver al menu")
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
    // Avisa a la clase padre que se selecciono
{
    var expanded by remember { mutableStateOf(false) }
    // Controla si la pantalla esta abierta o no

    Box {
        Button(onClick = { expanded = true }) {
            Text(selected)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            // Menu con dos las dos opciones de tarifa
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
