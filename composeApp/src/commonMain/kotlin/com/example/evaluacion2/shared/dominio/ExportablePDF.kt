package com.example.evaluacion2.shared.dominio

interface ExportablePDF {
    fun toPdfTable(): PdfTable
}