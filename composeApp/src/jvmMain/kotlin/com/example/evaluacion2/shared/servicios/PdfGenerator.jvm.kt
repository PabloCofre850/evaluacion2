package com.example.evaluacion2.shared.servicios

import com.example.evaluacion2.shared.dominio.*
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.font.Standard14Fonts
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.pow

actual class PdfGenerator {
    actual fun generarPdf(boletas: List<Boleta>, clientes: Map<String, Cliente>): ByteArray {
        val document = PDDocument()

        try {
            var page = PDPage(PDRectangle.A4)
            document.addPage(page)

            var contentStream = PDPageContentStream(document, page)
            var yPosition = 750f
            val margin = 50f

            // Título principal
            contentStream.beginText()
            contentStream.setFont(PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 18f)
            contentStream.newLineAtOffset(margin, yPosition)
            contentStream.showText("BOLETAS DE CONSUMO ELECTRICO")
            contentStream.endText()

            yPosition -= 30f

            boletas.forEach { boleta ->
                // Verificar si necesitamos una nueva página
                if (yPosition < 100f) {
                    contentStream.close()
                    page = PDPage(PDRectangle.A4)
                    document.addPage(page)
                    contentStream = PDPageContentStream(document, page)
                    yPosition = 750f
                }

                val cliente = clientes[boleta.idCliente]

                // Datos de la boleta
                val lineas = listOf(
                    "Cliente: ${cliente?.toString() ?: "Desconocido"}",
                    "RUT: ${boleta.idCliente}",
                    "Periodo: ${boleta.mes}/${boleta.anio}",
                    "Consumo total: ${boleta.kwhTotal.toStringSafe()} kWh",
                    "Tarifa: ${boleta.detalle.nombre}",
                    "Precio kWh: $${boleta.detalle.precioKwh.toStringSafe()}",
                    "Consumo: ${boleta.detalle.consumo.toStringSafe()} kWh",
                    "Total a pagar: $${boleta.detalle.total.toStringSafe()}",
                    "Estado: ${boleta.estado.name}"
                )

                contentStream.setFont(PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 11f)
                contentStream.beginText()
                contentStream.newLineAtOffset(margin, yPosition)
                contentStream.showText("Boleta #${boleta.idCliente}-${boleta.mes}-${boleta.anio}")
                contentStream.endText()
                yPosition -= 15f

                contentStream.setFont(PDType1Font(Standard14Fonts.FontName.HELVETICA), 10f)

                lineas.forEach { linea ->
                    contentStream.beginText()
                    contentStream.newLineAtOffset(margin + 10, yPosition)
                    contentStream.showText(linea)
                    contentStream.endText()
                    yPosition -= 15f
                }

                // Línea separadora
                contentStream.moveTo(margin, yPosition)
                contentStream.lineTo(PDRectangle.A4.width - margin, yPosition)
                contentStream.stroke()
                yPosition -= 20f
            }

            contentStream.close()

            val outputStream = ByteArrayOutputStream()
            document.save(outputStream)
            return outputStream.toByteArray()

        } finally {
            document.close()
        }
    }

    actual fun guardarPdf(bytes: ByteArray, nombreArchivo: String): String {
        val file = File(System.getProperty("user.home"), nombreArchivo)
        file.writeBytes(bytes)
        return file.absolutePath
    }

    actual fun guardarYAbrirPdf(bytes: ByteArray, nombreArchivo: String): String {
        val file = File(System.getProperty("user.home"), nombreArchivo)
        file.writeBytes(bytes)

        // Abrir el PDF automáticamente
        try {
            val desktop = java.awt.Desktop.getDesktop()
            if (desktop.isSupported(java.awt.Desktop.Action.OPEN)) {
                desktop.open(file)
            }
        } catch (e: Exception) {
            println("No se pudo abrir el PDF automáticamente: ${e.message}")
        }

        return file.absolutePath
    }

    private fun Double.toStringSafe(decimals: Int = 2): String {
        val factor = 10.0.pow(decimals)
        val rounded = kotlin.math.round(this * factor) / factor
        return rounded.toString()
    }
}
