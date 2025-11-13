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
import com.example.evaluacion2.shared.persistencia.ClienteRepoImpl
import com.example.evaluacion2.shared.persistencia.ClienteRepositorio
import com.example.evaluacion2.shared.persistencia.MedidorRepoImpl
import com.example.evaluacion2.shared.persistencia.MedidorRepositorio

private val CGEBlue      = Color(0xFF4A148C)

@Composable
fun PantallaLecturas(
    onVolver: () -> Unit
) {
    val repo = remember { LecturaRepoImpl(PersistenciaDatos(StorageDriver())) }
    val clienteRepo: ClienteRepositorio = remember { ClienteRepoImpl(PersistenciaDatos(StorageDriver())) }
    val medidorRepo: MedidorRepositorio = remember { MedidorRepoImpl(PersistenciaDatos(StorageDriver())) }

    // --------- FILTRO O CONSULTA DE LECTURAS -------------
    var filtroRutCliente by remember { mutableStateOf("") }
    var filtroMedidor by remember { mutableStateOf("") } // ID (código) del medidor
    var filtroAnio    by remember { mutableStateOf("") }
    var filtroMes     by remember { mutableStateOf("") }
    var lecturas      by remember { mutableStateOf(listOf<LecturaConsumo>()) }
    var mostrandoFormulario by remember { mutableStateOf(false) }

    // -------Errores en los filtrados -------
    var errorRutFiltro by remember { mutableStateOf<String?>(null) }
    var errorMedidorFiltro by remember { mutableStateOf<String?>(null) }

    // recarga cada vez que cambian los filtros
    LaunchedEffect(filtroRutCliente, filtroMedidor, filtroAnio, filtroMes) {
        val rut = filtroRutCliente.trim()
        val id  = filtroMedidor.trim()
        val a = filtroAnio.toIntOrNull() ?: 0
        val m = filtroMes.toIntOrNull() ?: 0

        // Requisitos: RUT valido y existente + ID de medidor existente
        if (rut.isBlank()) {
            errorRutFiltro = null
            errorMedidorFiltro = null
            lecturas = emptyList()
            return@LaunchedEffect
        }

        val cliente = clienteRepo.obtenerPorRut(rut)

        if (cliente == null) {
            errorRutFiltro = "El RUT ingresado no corresponde a un cliente registrado."
            errorMedidorFiltro = null
            lecturas = emptyList()
            return@LaunchedEffect
        } else {
            errorRutFiltro = null
        }

        if (id.isBlank()) {
            errorMedidorFiltro = "Debe ingresar el ID (codigo) de un medidor."
            lecturas = emptyList()
            return@LaunchedEffect
        }

        val medidor = medidorRepo.obtenerPorCodigo(id)
        if (medidor == null) {
            errorMedidorFiltro = "El ID de medidor ingresado no existe."
            lecturas = emptyList()
            return@LaunchedEffect
        } else {
            errorMedidorFiltro = null
        }

        lecturas = if (a > 0 && m in 1..12) {
            repo.listarPorMedidorMes(id, a, m)
        } else {
            emptyList()
        }
    }

    // --------- ESTADOS DEL FORMULARIO -----------
    var formRutCliente by remember { mutableStateOf("") }
    var formIdMedidor by remember { mutableStateOf("") }   // ID (codigo) del medidor
    var formAnio      by remember { mutableStateOf("") }   // Año
    var formMes       by remember { mutableStateOf("") }
    var formKwh       by remember { mutableStateOf("") }
    var errorFormRut  by remember { mutableStateOf<String?>(null) }
    var errorFormMedidor by remember { mutableStateOf<String?>(null) }

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
            value = filtroRutCliente,
            onValueChange = { filtroRutCliente = it },
            label = { Text("RUT Cliente") },
            singleLine = true,
            isError = errorRutFiltro != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        if (errorRutFiltro != null) {
            Text(errorRutFiltro!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = filtroMedidor,
            onValueChange = { filtroMedidor = it },
            label = { Text("ID (Codigo) del Medidor") },
            singleLine = true,
            isError = errorMedidorFiltro != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        if (errorMedidorFiltro != null) {
            Text(errorMedidorFiltro!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(8.dp))
        }

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
                label = { Text("Mes (1-12)") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            // Al abrir el diálogo inicializa sus campos desde los filtros
            formRutCliente = filtroRutCliente
            formIdMedidor = filtroMedidor
            formAnio      = filtroAnio
            formMes       = filtroMes
            formKwh       = ""
            errorFormRut = null
            errorFormMedidor = null
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
                    Text("Mes (1-12)",        Modifier.weight(1f), style = MaterialTheme.typography.labelLarge)
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
                        value = formRutCliente,
                        onValueChange = { formRutCliente = it; errorFormRut = null },
                        label = { Text("RUT Cliente") },
                        singleLine = true,
                        isError = errorFormRut != null
                    )
                    if (errorFormRut != null) {
                        Spacer(Modifier.height(4.dp))
                        Text(errorFormRut!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = formIdMedidor,
                        onValueChange = { formIdMedidor = it; errorFormMedidor = null },
                        label = { Text("ID (Codigo) del Medidor") },
                        singleLine = true,
                        isError = errorFormMedidor != null
                    )
                    if (errorFormMedidor != null) {
                        Spacer(Modifier.height(4.dp))
                        Text(errorFormMedidor!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
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
                        label = { Text("Mes (1-12)") },
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
                    val rut = formRutCliente.trim()
                    val id  = formIdMedidor.trim()
                    val a = formAnio.toIntOrNull()
                    val m = formMes.toIntOrNull()
                    val k = formKwh.toDoubleOrNull()

                    // Validaciones: RUT y Medidor deben estar registrados
                    if (rut.isBlank()) {
                        errorFormRut = "Debe ingresar un RUT."
                        return@Button
                    }
                    if (clienteRepo.obtenerPorRut(rut) == null) {
                        errorFormRut = "El RUT ingresado no corresponde a un cliente registrado."
                        return@Button
                    } else {
                        errorFormRut = null
                    }

                    if (id.isBlank()) {
                        errorFormMedidor = "Debe ingresar el ID (codigo) del medidor."
                        return@Button
                    }
                    if (medidorRepo.obtenerPorCodigo(id) == null) {
                        errorFormMedidor = "El ID del medidor ingresado no existe."
                        return@Button
                    } else {
                        errorFormMedidor = null
                    }

                    if (a != null && m != null && k != null) {
                        repo.registrar(LecturaConsumo(id, a, m, k))
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

