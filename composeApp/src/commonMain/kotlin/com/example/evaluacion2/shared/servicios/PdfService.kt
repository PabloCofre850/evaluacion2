package com.example.evaluacion2.shared.servicios

import com.example.evaluacion2.shared.dominio.*
import kotlin.math.pow

class PdfService {

    fun generarBoletasPdf(boletas: List<Boleta>, clientes: Map<String, Cliente>): ByteArray {
        val contenido = buildString {
            appendLine("=== BOLETAS DE CONSUMO ELÉCTRICO ===")
            appendLine()

            boletas.forEach { b ->
                val cliente = clientes[b.idCliente]
                appendLine("Cliente: ${cliente?.toString() ?: "Desconocido"}")
                appendLine("RUT: ${b.idCliente}")
                appendLine("Año: ${b.anio}")
                appendLine("Mes: ${b.mes}")
                appendLine("Consumo total: ${b.kwhTotal.toStringSafe()} kWh")
                appendLine("Tarifa: ${b.detalle.nombre}")
                appendLine("Precio kWh: ${b.detalle.precioKwh.toStringSafe()}")
                appendLine("Consumo: ${b.detalle.consumo.toStringSafe()}")
                appendLine("Total: ${b.detalle.total.toStringSafe()}")
                appendLine("Estado: ${b.estado.name}")
                appendLine("--------------------------------------")
                appendLine()
            }
        }

        return contenido.encodeToByteArray()
    }

    private fun Double.toStringSafe(decimals: Int = 2): String {
        val factor = 10.0.pow(decimals)
        val rounded = kotlin.math.round(this * factor) / factor
        return rounded.toString()
    }
}
