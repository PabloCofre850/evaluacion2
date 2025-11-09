package com.example.evaluacion2.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.evaluacion2.shared.dominio.Medidor
import com.example.evaluacion2.shared.dominio.MedidorMonofasico
import com.example.evaluacion2.shared.dominio.MedidorTrifasico
import com.example.evaluacion2.shared.persistencia.MedidorRepoImpl
import com.example.evaluacion2.shared.persistencia.PersistenciaDatos
import com.example.evaluacion2.shared.persistencia.StorageDriver
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.example.evaluacion2.shared.persistencia.MedidorRepositorio

@Composable
fun PantallaMedidores(
    repo: MedidorRepositorio,
    onVolver: () -> Unit
) {
    var rutCliente by remember { mutableStateOf("") }

    var filtroValor by remember { mutableStateOf("") }

    var mostrandoFormulario by remember { mutableStateOf(false) }

    var seleccionado by remember { mutableStateOf<Medidor?>(null) }

    var medidores by remember { mutableStateOf(listOf<Medidor>()) }

    LaunchedEffect(rutCliente, mostrandoFormulario) {
        medidores = repo.listarPorCliente(rutCliente)
        seleccionado = null
    }

    Column(
        Modifier
            .fillMaxSize()
            .safeContentPadding()
            .padding(20.dp)
    ) {
        Text("Gestion de Medidores", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))

        // —— CAMPO RUT + BOTÓN CARGAR ——                                 <=
        OutlinedTextField(
            value = rutCliente,
            onValueChange = { rutCliente = it },
            label = { Text("RUT Cliente") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        if (mostrandoFormulario) {
            FormularioMedidor(
                onGuardar = { nuevo ->
                    repo.crear(nuevo, rutCliente)
                    mostrandoFormulario = false
                },
                onCancelar = { mostrandoFormulario = false }
            )

        } else {
            // --- Filtra por codiog ---
            OutlinedTextField(
                value = filtroValor,
                onValueChange = { filtroValor = it },
                label = { Text("Filtrar por codigo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // -- Listado filtrado solo en UI ---
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
                    .padding(8.dp)
            ) {
                item {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Código",    fontWeight = FontWeight.Bold)
                        Text("Dirección", fontWeight = FontWeight.Bold)
                        Text("Activo",    fontWeight = FontWeight.Bold)
                        Text("Tipo",      fontWeight = FontWeight.Bold)
                    }
                    Divider()
                }

                items(
                    medidores.filter { filtroValor.isBlank() ||
                            it.codigo.contains(filtroValor, ignoreCase = true)
                    }
                ) { medidor ->
                    val isSelected = medidor == seleccionado
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { seleccionado = medidor }
                            .padding(vertical = 4.dp)
                            .border(
                                width = if (isSelected) 2.dp else 0.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(medidor.codigo)
                        Text(medidor.direccionSuministro)
                        Text(if (medidor.activo) "Sí" else "No")
                        Text(medidor.tipo())
                    }
                    Divider()
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = {
                        seleccionado?.let { toDelete ->
                            if (repo.eliminar(toDelete.codigo)) {
                                medidores = medidores.filter { it.codigo != toDelete.codigo }
                                seleccionado = null
                            }
                        }
                    },
                    enabled = (seleccionado != null),
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
            Button(onClick = onVolver) {
                Text("Volver al menú")
            }
        }
    }
}



// Formulario de creacion con seleccion de "Activo / No activo"
@Composable
private fun FormularioMedidor(
    onGuardar: (Medidor) -> Unit,
    onCancelar: () -> Unit
) {
    var codigo by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var tipoMono by remember { mutableStateOf(true) }
    var activo by remember { mutableStateOf<Boolean?>(null) }
    var potenciaMaxKw by remember { mutableStateOf("") }
    var factorPotencia by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Nuevo Medidor", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            value = codigo,
            onValueChange = { codigo = it },
            label = { Text("Codigo del medidor") })

        OutlinedTextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text("Direccion suministro") })

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
            RadioButton(selected = tipoMono == true, onClick = { tipoMono = true })
            Text("Monofasico")
            RadioButton(selected = tipoMono == false, onClick = { tipoMono = false })
            Text("Trifasico")
        }

        OutlinedTextField(
            value = potenciaMaxKw,
            onValueChange = { potenciaMaxKw = it },
            label = { Text("Potencia Máx (kW)") }
        )

        if (!tipoMono) {
            OutlinedTextField(
                value = factorPotencia,
                onValueChange = { factorPotencia = it },
                label = { Text("Factor Potencia") })
        }

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = onCancelar) {
                Text("Salir") }
            Button(
                onClick = {
                    if (codigo.isNotBlank() && direccion.isNotBlank() && activo != null) {
                        val medidor = if (tipoMono) {
                            MedidorMonofasico(
                                codigo = codigo,
                                direccionSuministro = direccion,
                                activo = activo!!,
                                potenciaMaxKw = potenciaMaxKw.toDouble()
                            )
                    } else {
                        MedidorTrifasico(
                            codigo = codigo,
                            direccionSuministro = direccion,
                            activo = activo!!,
                            potenciaMaxKw = potenciaMaxKw.toDouble(),
                            factorPotencia = factorPotencia.toDoubleOrNull() ?: 1.0
                        )
                    }
                        onGuardar(medidor)
                    }
                },
                enabled = codigo.isNotBlank() && direccion.isNotBlank() && activo != null && potenciaMaxKw.toDoubleOrNull() != null
            ) {
                Text("Guardar")
            }
        }
    }
}


@Preview
@Composable
private fun PantallaMedidoresPreview() {
    val previewRepo = remember {
        MedidorRepoImpl(PersistenciaDatos(StorageDriver()))
    }
    PantallaMedidores(
        repo = previewRepo,
        onVolver = {}
    )
}
