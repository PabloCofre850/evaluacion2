package com.example.evaluacion2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.evaluacion2.shared.persistencia.ClienteRepoImpl
import com.example.evaluacion2.shared.persistencia.ClienteRepositorio
import com.example.evaluacion2.shared.persistencia.PersistenciaDatos
import com.example.evaluacion2.shared.persistencia.StorageDriver
import com.example.evaluacion2.shared.dominio.Cliente as ClienteDomain
import com.example.evaluacion2.shared.dominio.EstadoCliente

private data class ClienteUI(
    val rut: String,
    val nombre: String,
    val email: String,
    val direccion: String,
    val estado: String
)

private fun ClienteDomain.toUI(): ClienteUI =
    ClienteUI(
        rut = rut,
        nombre = nombre,
        email = email,
        direccion = direccionFacturacion,
        estado = estado.name
    )

private fun ClienteUI.toDomain(defaultEstado: EstadoCliente = EstadoCliente.ACTIVO): ClienteDomain =
    ClienteDomain(
        run = rut,
        nombre = nombre,
        email = email,
        direccionFacturacion = direccion,
        estado = runCatching { EstadoCliente.valueOf(estado) }.getOrElse { defaultEstado },
        tipo = "RESIDENCIAL"
    )

private sealed class FormMode {
    data object None : FormMode()
    data object New : FormMode()
    data class Edit(val originalRut: String) : FormMode()
}

private val CGEBlue      = Color(0xFF4A148C)
@Composable
fun PantallaClientes(
    onVolver: () -> Unit
) {
    val repo: ClienteRepositorio = remember {
        ClienteRepoImpl(PersistenciaDatos(StorageDriver()))
    }

    val clientes = remember { mutableStateListOf<ClienteUI>() }

    LaunchedEffect(Unit) {
        val data = repo.listar()
        clientes.clear()
        clientes.addAll(data.map { it.toUI() })
    }

    fun recargarClientes() {
        val data = repo.listar()
        clientes.clear()
        clientes.addAll(data.map { it.toUI() })
    }

    var filtroRut by remember { mutableStateOf("") }
    var seleccionado: ClienteUI? by remember { mutableStateOf(null) }
    var modo by remember { mutableStateOf<FormMode>(FormMode.None) }

    val clientesFiltrados = clientes.filter {
        filtroRut.isBlank() || it.rut.contains(filtroRut, ignoreCase = true)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CGEBlue)  // color de la “barra”
            .padding(vertical = 8.dp, horizontal = 100.dp)            // espacio dentro
    ){
        Text("Registrar Clientes", fontSize = 40.sp, color = Color.White, fontWeight = FontWeight.Bold)

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(20.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
            ) {
                Spacer(Modifier.height(60.dp))
                Text("Filtro Rut", style = MaterialTheme.typography.bodyLarge)

                OutlinedTextField(
                    value = filtroRut,
                    onValueChange = {
                        filtroRut = it
                        if (seleccionado != null && !seleccionado!!.rut.contains(it, ignoreCase = true)) {
                            seleccionado = null
                            if (modo is FormMode.Edit) modo = FormMode.None
                        }
                    },
                    label = { Text("Ingresar RUT (Ej: 12.345.678-9)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.6f)
                )

                Spacer(Modifier.height(32.dp))

                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Text("RUT", modifier = Modifier.weight(1f))
                    Text("Nombre", modifier = Modifier.weight(1f))
                    Text("Dirección", modifier = Modifier.weight(1f))
                    Text("Estado", modifier = Modifier.weight(0.7f))
                }
                Divider()

                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(items = clientesFiltrados, key = { it.rut }) { c ->
                        val isSelected = seleccionado?.rut == c.rut
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    seleccionado = if (isSelected) null else c
                                }
                                .background(
                                    if (isSelected)
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                                    else
                                        Color.Transparent
                                )
                                .padding(vertical = 8.dp)
                        ) {
                            Text(c.rut, modifier = Modifier.weight(1f))
                            Text(c.nombre, modifier = Modifier.weight(1f))
                            Text(c.direccion, modifier = Modifier.weight(1f))
                            Text(c.estado, modifier = Modifier.weight(0.7f))
                        }
                        Divider()
                    }
                }

                Spacer(Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(onClick = {
                        seleccionado = null
                        modo = FormMode.New
                    }) { Text("Nuevo cliente") }

                    Button(
                        onClick = {
                            seleccionado?.let { sel -> modo = FormMode.Edit(originalRut = sel.rut) }
                        },
                        enabled = seleccionado != null
                    ) { Text("Actualizar cliente") }

                    Button(
                        onClick = {
                            seleccionado?.let { sel ->
                                if (repo.eliminar(sel.rut)) {
                                    recargarClientes()
                                }
                                seleccionado = null
                                if (modo is FormMode.Edit) modo = FormMode.None
                            }
                        },
                        enabled = seleccionado != null
                    ) { Text("Eliminar") }
                }
            }

            Spacer(Modifier.width(40.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (val m = modo) {
                    is FormMode.None -> {}
                    is FormMode.New, is FormMode.Edit -> {
                        val inicial = if (m is FormMode.Edit) seleccionado else null
                        var rutCampo by remember(m) { mutableStateOf(inicial?.rut ?: "") }
                        var nombreCampo by remember(m) { mutableStateOf(inicial?.nombre ?: "") }
                        var emailCampo by remember(m) { mutableStateOf(inicial?.email ?: "") }
                        var direccionCampo by remember(m) { mutableStateOf(inicial?.direccion ?: "") }

                        Text("Ingrese los datos del cliente", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(rutCampo, { rutCampo = it }, label = { Text("Rut") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(nombreCampo, { nombreCampo = it }, label = { Text("Nombre") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(emailCampo, { emailCampo = it }, label = { Text("Email") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(direccionCampo, { direccionCampo = it }, label = { Text("Dirección de Facturación") }, singleLine = true, modifier = Modifier.fillMaxWidth())

                        Spacer(Modifier.height(16.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(onClick = {
                                if (rutCampo.isBlank() || nombreCampo.isBlank() || emailCampo.isBlank() || direccionCampo.isBlank()) {
                                    scope.launch { snackbarHostState.showSnackbar("Faltan campos por rellenar") }
                                    return@Button
                                }

                                when (m) {
                                    is FormMode.New -> {
                                        repo.crear(
                                            ClienteDomain(
                                                run = rutCampo,
                                                nombre = nombreCampo,
                                                email = emailCampo,
                                                direccionFacturacion = direccionCampo,
                                                estado = EstadoCliente.ACTIVO,
                                                tipo = "RESIDENCIAL"
                                            )
                                        )
                                        recargarClientes()
                                        seleccionado = clientes.find { it.rut == rutCampo }
                                        modo = FormMode.None
                                    }

                                    is FormMode.Edit -> {
                                        if (rutCampo.isBlank() || nombreCampo.isBlank() || emailCampo.isBlank() || direccionCampo.isBlank()) {
                                            scope.launch { snackbarHostState.showSnackbar("Faltan campos por rellenar para actualizar") }
                                            return@Button
                                        }

                                        val estadoActual = runCatching {
                                            EstadoCliente.valueOf(seleccionado?.estado ?: "ACTIVO")
                                        }.getOrElse { EstadoCliente.ACTIVO }

                                        if (m.originalRut != rutCampo) {
                                            repo.crear(
                                                ClienteDomain(
                                                    run = rutCampo,
                                                    nombre = nombreCampo,
                                                    email = emailCampo,
                                                    direccionFacturacion = direccionCampo,
                                                    estado = estadoActual,
                                                    tipo = "RESIDENCIAL"
                                                )
                                            )
                                            repo.eliminar(m.originalRut)
                                        } else {
                                            repo.actualizar(
                                                ClienteDomain(
                                                    run = rutCampo,
                                                    nombre = nombreCampo,
                                                    email = emailCampo,
                                                    direccionFacturacion = direccionCampo,
                                                    estado = estadoActual,
                                                    tipo = "RESIDENCIAL"
                                                )
                                            )
                                        }
                                        recargarClientes()
                                        seleccionado = clientes.find { it.rut == rutCampo }
                                        modo = FormMode.None
                                    }
                                    else -> Unit
                                }
                            }) {
                                Text("Guardar")
                            }
                            Button(onClick = { modo = FormMode.None }) {
                                Text("Cancelar")
                            }
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        Button(
            onClick = onVolver,
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Text("Volver al menú")
        }
    }
}