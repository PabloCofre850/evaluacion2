package com.example.evaluacion2.shared.dominio

import kotlinx.serialization.Serializable

// Representa a un cliente del sistema electrico.
// Es una "data" class porque solo almacena datos, sin logica.
@Serializable
data class Cliente(
    override var run: String,                 // Se usa como RUT del titular
    override var nombre: String,              // Nombre de la persona titular
    override var email: String,               // Email de la persona titular
    val direccionFacturacion: String,         // Direccion
    val estado: EstadoCliente,                // Estado de la tarifa
    val tipo: String                          // Tipo de tarifa
) : Persona(
    run = run,
    nombre = nombre,
    email = email
) {
    val rut: String get() = run
}
