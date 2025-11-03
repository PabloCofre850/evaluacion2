package com.example.evaluacion2.shared.dominio

import com.example.evaluacion2.shared.dominio.TarifaDetalle

class Boleta (
    val idCliente: String,
    var anio: Int,
    var mes: Int,
    var kwhTotal:Double,
    var detalle: TarifaDetalle,
    var estado: EstadoBoleta
){
    fun toPdfTable(): PdfTable{
        TODO("plox")
    }
}