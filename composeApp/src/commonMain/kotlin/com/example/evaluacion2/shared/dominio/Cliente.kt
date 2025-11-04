package com.example.evaluacion2.shared.dominio

import kotlinx.serialization.Serializable

// Representa a un cliente del sistema electrico.
// Es una "data" class porque solo almacena datos, sin logica.
@Serializable

data class Cliente(
    val rut: String,  // Rut titular
    val direccionFacturacion: String,  // Direccion
    val estado: EstadoCliente,  // Estado de la tarifa
    val tipo: String  // Tipo de tarifa
)
