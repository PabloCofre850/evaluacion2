package com.example.evaluacion2.shared.dominio

// Importamos esto para poder convertir la clase a JSON fácilmente

import kotlinx.serialization.Serializable

// La clase Boleta representa una boleta de consumo eléctrico emitida a un cliente.
// Es una 'data class' porque solo guarda información (no tiene lógica).

@Serializable // 👈 Necesario para guardar y/o leer como JSON
data class Boleta(
    val idCliente: String,
    val anio: Int,
    val mes: Int,
    val kwhTotal: Double,
    val detalle: TarifaDetalle,
    val estado: EstadoBoleta
)
