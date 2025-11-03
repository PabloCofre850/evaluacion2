package com.example.evaluacion2.shared.servicios

import com.example.evaluacion2.shared.persistencia.BoletaRepositorio
import com.example.evaluacion2.shared.persistencia.ClienteRepositorio
import com.example.evaluacion2.shared.persistencia.LecturaRepositorio
import com.example.evaluacion2.shared.persistencia.MedidorRepositorio
import com.example.evaluacion2.shared.dominio.Boleta

class BoletaService (
    var clientes: ClienteRepositorio,
    var medidores: MedidorRepositorio,
    var lecturas: LecturaRepositorio,
    var boletas: BoletaRepositorio,
    var tarifas: TarifaService
){
    fun emitirBoletaMensual(rutCliente: String, anio: Int, mes: Int): Boleta{
        TODO("CHUPALO")
    }
    fun calcularKwhClienteMes(rutCliente: String, anio: Int, mes: Int): Double{
        TODO("CHUPALO")
    }
    fun exportarPdfClienteMes(rutCliente: String, anio: Int, mes: Int, pdf: PdfService): ByteArray{
        TODO("CHUPALO")
    }
}
