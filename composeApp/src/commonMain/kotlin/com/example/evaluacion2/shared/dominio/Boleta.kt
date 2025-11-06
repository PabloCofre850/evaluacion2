package com.example.evaluacion2.shared.dominio

// Importamos esto para poder convertir la clase a JSON fácilmente
import kotlin.math.pow

import kotlinx.serialization.Serializable



// La clase Boleta representa una boleta de consumo electrico emitida a un cliente.
// Es una "data class" porque solo guarda informacion (no tiene logica).

@Serializable // Necesario para guardar y/o leer
data class Boleta(
    val idCliente: String,
    val anio: Int,
    val mes: Int,
    val kwhTotal: Double,
    val detalle: TarifaDetalle,
    val estado: EstadoBoleta
): ExportablePDF {

    // Implementación de la funcion para exportar la boleta como tabla PDF
    override fun toPdfTable(): PdfTable {

        val headers = listOf("Campo", "Valor")

        // Funcion auxiliar para redondear y convertir Double a String con seguridad
        //ya que no nos dejo utiizar String.format o .format

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
