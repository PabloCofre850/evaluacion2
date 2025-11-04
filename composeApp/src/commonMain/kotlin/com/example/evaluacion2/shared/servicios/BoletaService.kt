package com.example.evaluacion2.shared.servicios

import com.example.evaluacion2.shared.persistencia.BoletaRepositorio
import com.example.evaluacion2.shared.persistencia.ClienteRepositorio
import com.example.evaluacion2.shared.persistencia.LecturaRepositorio
import com.example.evaluacion2.shared.persistencia.MedidorRepositorio
import com.example.evaluacion2.shared.dominio.Boleta

class BoletaService (
    //les puse private porque aparece asi en el UML//
    private val clientes: ClienteRepositorio,
    private val medidores: MedidorRepositorio,
    private val lecturas: LecturaRepositorio,
    private val boletas: BoletaRepositorio,
    private val tarifas: TarifaService
){
    fun emitirBoletaMensual(rutCliente: String, anio: Int, mes: Int): Boleta{
        val cliente= clientes.obtenerPorRut(rutCliente) ?: throw Exception("Cliente no encontrado")

        val consumo = calcularKwhClienteMes(rutCliente, anio, mes)
        val tarifa = tarifas.tarifaPara(cliente)
        val detalle = tarifa.detalleParaConsumo(consumo)

        val boleta = Boleta(
            idCliente = rutCliente,
            anio = anio,
            mes = mes,
            kwhTotal = consumo,
            detalle = detalle,
            estado = Boleta.EstadoBoleta.EMITIDA
        )

        boleta.guardar(boletas)
        return boleta
    }
    fun calcularKwhClienteMes(rutCliente: String, anio: Int, mes: Int): Double{
        TODO("CHUPALO")
    }
    fun exportarPdfClienteMes(rutCliente: String, anio: Int, mes: Int, pdf: PdfService): ByteArray{
        TODO("CHUPALO")
    }
}
