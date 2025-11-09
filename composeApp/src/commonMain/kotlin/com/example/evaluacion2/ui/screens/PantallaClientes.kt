package com.example.evaluacion2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private data class ClienteUI(
    val rut: String,
    val nombre: String,
    val email: String,
    val direccion: String,
    val estado: String
)

private sealed class FormMode {
    data object None : FormMode()
    data object New : FormMode()
    data class Edit(val originalRut: String) : FormMode()
}

@Composable
fun PantallaClientes(
    onVolver: () -> Unit
){
    // Datos de ejemplo (luego se reemplazan por ClienteRepositorio)
    val clientes = remember {
        mutableStateListOf(
            ClienteUI("11.111.111-1", "Juan Pérez", "juan@example.com", "Av. Siempre Viva 123", "ACTIVO"),
            ClienteUI("22.222.222-2", "María Gómez", "maria@example.com", "Calle Falsa 456", "INACTIVO"),
            ClienteUI("33.333.333-3", "Luis Soto", "luis@example.com", "Pasaje Central 789", "ACTIVO")
        )
    }

    var filtroRut by remember { mutableStateOf("") }  // Guarda rut observable
    var seleccionado: ClienteUI? by remember { mutableStateOf(null) } // Estado fila seleccionada
    var modo by remember { mutableStateOf<FormMode>(FormMode.None) }

    val clientesFiltrados = clientes.filter {
        filtroRut.isBlank() || it.rut.contains(filtroRut, ignoreCase = true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
    ) {
        Row( // Fila principal centrada verticalmente
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column( // Columna izquierda: título, filtro, tabla y acciones
                modifier = Modifier
                    .weight(2f).padding(20.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
            ) {
                // Título
                Text("Clientes registrados", style = MaterialTheme.typography.headlineSmall)

                Spacer(Modifier.height(60.dp))

                Row {
                    Text("Filtro Rut", style = MaterialTheme.typography.bodyLarge)
                }

                // Fila de filtro por RUT
                Row(modifier = Modifier.fillMaxWidth()) {
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
                }

                Spacer(Modifier.height(32.dp))

                // Encabezados de "tabla"
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Text("RUT", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelLarge)
                    Text("Nombre", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelLarge)
                    Text("Dirección", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelLarge)
                    Text("Estado", modifier = Modifier.weight(0.7f), style = MaterialTheme.typography.labelLarge)
                }
                Divider()

                // Lista (tabla) seleccionable
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
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

                // Botones de acciones (debajo de la tabla)
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
                                clientes.removeAll { it.rut == sel.rut }
                                seleccionado = null
                                if (modo is FormMode.Edit) modo = FormMode.None
                            }
                        },
                        enabled = seleccionado != null
                    ) { Text("Eliminar") }
                }
            }

            Spacer(Modifier.width(40.dp))

            Column( // Columna derecha (formulario cuando aplique)
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (val m = modo) {
                    is FormMode.None -> {
                        // No mostrar nada si no hay acción
                    }
                    is FormMode.New, is FormMode.Edit -> {
                        // Inicializa campos según modo (New vacío, Edit con selección actual)
                        val inicial = if (m is FormMode.Edit) seleccionado else null
                        var rutCampo by remember(m) { mutableStateOf(inicial?.rut ?: "") }
                        var nombreCampo by remember(m) { mutableStateOf(inicial?.nombre ?: "") }
                        var emailCampo by remember(m) { mutableStateOf(inicial?.email ?: "") }
                        var direccionCampo by remember(m) { mutableStateOf(inicial?.direccion ?: "") }

                        Text("Ingrese datos del cliente", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = rutCampo,
                            onValueChange = { rutCampo = it },
                            label = { Text("Rut") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = nombreCampo,
                            onValueChange = { nombreCampo = it },
                            label = { Text("Nombre") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = emailCampo,
                            onValueChange = { emailCampo = it },
                            label = { Text("Email") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = direccionCampo,
                            onValueChange = { direccionCampo = it },
                            label = { Text("Dirección de Facturación") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(16.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(onClick = {
                                when (m) {
                                    is FormMode.New -> {
                                        // Agregar nuevo
                                        clientes.add(
                                            ClienteUI(
                                                rut = rutCampo,
                                                nombre = nombreCampo,
                                                email = emailCampo,
                                                direccion = direccionCampo,
                                                estado = "ACTIVO"
                                            )
                                        )
                                        seleccionado = clientes.find { it.rut == rutCampo }
                                        modo = FormMode.None
                                    }
                                    is FormMode.Edit -> {
                                        // Actualizar existente (busca por rut original)
                                        val idx = clientes.indexOfFirst { it.rut == m.originalRut }
                                        if (idx >= 0) {
                                            val anterior = clientes[idx]
                                            val actualizado = anterior.copy(
                                                rut = rutCampo,
                                                nombre = nombreCampo,
                                                email = emailCampo,
                                                direccion = direccionCampo
                                            )
                                            clientes[idx] = actualizado
                                            seleccionado = actualizado
                                        }
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

        // Botón fijo abajo a la izquierda
        Button(
            onClick = onVolver,
            modifier = Modifier.align(Alignment.BottomStart) // Ancla a la izq inferior
        ) {
            Text("Volver al menú")
        }
    }
}

