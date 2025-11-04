package com.example.evaluacion2.shared.dominio

import kotlinx.serialization.Serializable

// Representa la tarifa comercial.
// Similar a la residencial, pero con un recargo adicional sobre el total.

@Serializable

data class TarifaComercial(

    override val nombre: String = "Comercial",
    val cargoFijo: Double = 8000.0,   // Cargo fijo base más alto
    val precioKwh: Double = 150.0,    // Precio por kWh más caro
    val recargo: Double = 0.10,       // Recargo adicional (10%)
    val iva: Double = 0.19            // IVA del 19%

) : Tarifa {

    // Calcula el monto total aplicando IVA y recargo.


    override fun calcular(kwh: Double): TarifaDetalle {
        val subtotal = cargoFijo + (kwh * precioKwh)  // Calculamos el subtotal
        val total = subtotal * (1 + iva) * (1 + recargo)  // Calculamos el total

        // Retorna el detalle al completo de la tarifa

        return TarifaDetalle(
            nombre = nombre,
            precioKwh = precioKwh,
            consumo = kwh,
            total = total
        )
    }
}
