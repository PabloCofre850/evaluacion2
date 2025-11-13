package com.example.evaluacion2.shared.dominio

import kotlinx.serialization.Serializable


// Representa una tarifa residencial.
// Calcula el total a pagar sumando el cargo fijo + consumo * precioKwh,
// y luego aplica IVA (porcentaje).

@Serializable // Permite convertir esta clase a JSON (necesario para Boleta)

data class TarifaResidencial(

    override val nombre: String = "Residencial",
    val cargoFijo: Double = 5000.0,   // monto fijo que se cobra siempre
    val precioKwh: Double = 120.0,    // costo por kWh consumido
    val iva: Double = 0.19            // 19% de impuesto
) : Tarifa {


    override fun calcular(kwh: Double): TarifaDetalle {

        val subtotal = cargoFijo + (kwh * precioKwh)
        val total = subtotal * (1 + iva)

        // Retorna el detalle al completo de la tarifa

        return TarifaDetalle(
            nombre = nombre,
            precioKwh = precioKwh,
            consumo = kwh,
            total = total
        )
    }
}
