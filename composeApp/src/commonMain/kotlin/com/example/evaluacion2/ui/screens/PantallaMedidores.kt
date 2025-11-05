package com.example.evaluacion2.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PantallaMedidores(
    onVolver: () -> Unit
) {
    var filtroValor by remember { mutableStateOf("") }
    var mostrandoFormulario by remember { mutableStateOf(false) }

    var medidores by remember {

        // Datos de ejemplo iniciales
        mutableStateOf(
            listOf(
                MedidorUi("M001", "RUT001", "Av. Los Álamos 321", true, "Monofásico"),
                MedidorUi("M002", "RUT002", "Calle Central 987", false, "Trifásico"),
                MedidorUi("M003", "RUT003", "Ruta 5 Norte", true, "Monofásico")
            )
        )
    }

    // Medidor seleccionado en la lista
    var seleccionado by remember { mutableStateOf<MedidorUi?>(null) }


    //si está mostrando el formulario, lo mostramos
    if (mostrandoFormulario) {
        FormularioMedidor(
            onGuardar = { nuevo ->
                medidores = medidores + nuevo
                mostrandoFormulario = false
            },
            onCancelar = { mostrandoFormulario = false }
        )

    // si no, mostramos la lista
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
                .padding(20.dp)
        ) {
            Text("Gestión de Medidores", style = MaterialTheme.typography.headlineSmall)

            Spacer(Modifier.height(16.dp))
            Text("Medidores registrados", fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))

            // Campo de filtro por código o RUT

            OutlinedTextField(
                value = filtroValor,
                onValueChange = { filtroValor = it },
                label = { Text("Filtrar por código o RUT cliente") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
                    .padding(8.dp)
            ) {
                item {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Código", fontWeight = FontWeight.Bold)
                        Text("RUT Cliente", fontWeight = FontWeight.Bold)
                        Text("Dirección", fontWeight = FontWeight.Bold)
                        Text("Activo", fontWeight = FontWeight.Bold)
                        Text("Tipo", fontWeight = FontWeight.Bold)
                    }
                    Divider()
                }

                items(medidores.filter {

                    // Aplicamos el filtro si no está vacío
                    filtroValor.isBlank() ||

                            // buscamos en código o RUT cliente (del medidor)
                            it.codigo.contains(filtroValor, true) ||
                            it.rutCliente.contains(filtroValor, true)
                }) { medidor ->
                    val isSelected = seleccionado == medidor
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { seleccionado = medidor }
                            .padding(vertical = 4.dp)
                            .border(
                                width = if (isSelected) 2.dp else 0.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(medidor.codigo)
                        Text(medidor.rutCliente)
                        Text(medidor.direccion)
                        Text(if (medidor.activo) "Sí" else "No")
                        Text(medidor.tipo)
                    }
                    Divider()
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = {
                        seleccionado?.let { sel ->
                            medidores = medidores.filter { it != sel }
                            seleccionado = null
                        }
                    },
                    enabled = seleccionado != null,
                    modifier = Modifier.width(160.dp)
                ) {
                    Text("Eliminar Medidor")
                }

                Button(
                    onClick = { mostrandoFormulario = true },
                    modifier = Modifier.width(160.dp)
                ) {
                    Text("Crear Medidor")
                }
            }

            Spacer(Modifier.height(24.dp))
            Button(onClick = onVolver) { Text("Volver al menú") }
        }
    }
}


// Formulario de creación con selección de "Activo / No activo"
@Composable
fun FormularioMedidor(
    onGuardar: (MedidorUi) -> Unit,
    onCancelar: () -> Unit
) {
    var rutCliente by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("Monofásico") }
    var activo by remember { mutableStateOf<Boolean?>(null) }
    var potenciaMaxKw by remember { mutableStateOf("") }
    var factorPotencia by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Nuevo Medidor", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(value = rutCliente, onValueChange = { rutCliente = it }, label = { Text("RUT Cliente") })
        OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección suministro") })

        // Sección: Estado activo/inactivo

        Text("Estado del medidor:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            RadioButton(selected = activo == true, onClick = { activo = true })
            Text("Activo")
            RadioButton(selected = activo == false, onClick = { activo = false })
            Text("No activo")
        }


        // Tipo de medidor (Monofásico / Trifásico)
        Text("Tipo de medidor:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            RadioButton(selected = tipo == "Monofásico", onClick = { tipo = "Monofásico" })
            Text("Monofásico")
            RadioButton(selected = tipo == "Trifásico", onClick = { tipo = "Trifásico" })
            Text("Trifásico")
        }

        OutlinedTextField(value = potenciaMaxKw, onValueChange = { potenciaMaxKw = it }, label = { Text("Potencia Máx (kW)") })
        if (tipo == "Trifásico") {
            OutlinedTextField(value = factorPotencia, onValueChange = { factorPotencia = it }, label = { Text("Factor Potencia") })
        }

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = onCancelar) { Text("Salir") }
            Button(
                onClick = {
                    if (rutCliente.isNotBlank() && direccion.isNotBlank() && activo != null) {
                        val nuevo = MedidorUi(
                            codigo = "M${(100..999).random()}",
                            rutCliente = rutCliente,
                            direccion = direccion,
                            activo = activo!!,
                            tipo = tipo
                        )
                        onGuardar(nuevo)
                    }
                },
                enabled = activo != null
            ) {
                Text("Guardar")
            }
        }
    }
}


// Clase auxiliar (temporal para UI)

data class MedidorUi(
    val codigo: String,
    val rutCliente: String,
    val direccion: String,
    val activo: Boolean,
    val tipo: String
)

@org.jetbrains.compose.ui.tooling.preview.Preview
@Composable
private fun PantallaMedidoresPreview() {
    PantallaMedidores(onVolver = {})
}

