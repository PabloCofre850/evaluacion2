package com.example.evaluacion2.shared.dominio

import kotlin.math.pow

// Representa una boleta de consumo eléctrico emitida a un cliente.
data class Boleta(
    val idCliente: String,
    val anio: Int,
    val mes: Int,
    val kwhTotal: Double,
    val detalle: TarifaDetalle,
    val estado: EstadoBoleta
): ExportablePDF {

    // Convierte la boleta en una tabla para PDF (texto estructurado)
    override fun toPdfTable(): PdfTable {
        val headers = listOf("Campo", "Valor")

        fun Double.toStringSafe(decimals: Int = 2): String {
            val factor = 10.0.pow(decimals)
            val rounded = kotlin.math.round(this * factor) / factor
            return rounded.toString()
        }

        val rows = listOf(
            listOf("Cliente", idCliente),
            listOf("Año", anio.toString()),
            listOf("Mes", mes.toString()),
            listOf("kWh Total", kwhTotal.toStringSafe()),
            listOf("Tarifa", detalle.nombre),
            listOf("Precio kWh", detalle.precioKwh.toStringSafe()),
            listOf("Consumo", detalle.consumo.toStringSafe()),
            listOf("Total", detalle.total.toStringSafe()),
            listOf("Estado", estado.name)
        )

        return PdfTable(headers, rows)
    }
}
