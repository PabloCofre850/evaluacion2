package com.example.evaluacion2.shared.servicios

import com.example.evaluacion2.shared.dominio.Boleta
import com.example.evaluacion2.shared.dominio.Cliente

expect class PdfGenerator() {
    fun generarPdf(boletas: List<Boleta>, clientes: Map<String, Cliente>): ByteArray
    fun guardarPdf(bytes: ByteArray, nombreArchivo: String): String
    fun guardarYAbrirPdf(bytes: ByteArray, nombreArchivo: String): String
}
