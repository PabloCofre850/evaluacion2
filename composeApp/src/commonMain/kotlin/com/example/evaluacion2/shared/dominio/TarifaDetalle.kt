package com.example.evaluacion2.shared.dominio

import kotlinx.serialization.Serializable

@Serializable
data class TarifaDetalle(
    val nombre: String,      // Tipo de tarifa
    val precioKwh: Double,   // Precio por kWh
    val consumo: Double,     // kWh consumidos
    val total: Double        // Monto total a pagar
)
