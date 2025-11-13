package com.example.evaluacion2.shared.servicios

import com.example.evaluacion2.shared.persistencia.*
import com.example.evaluacion2.shared.dominio.*

class BoletaService(
    private val clientes: ClienteRepositorio,
    private val medidores: MedidorRepositorio,
    private val lecturas: LecturaRepositorio,
    private val boletas: BoletaRepositorio,
    private val tarifas: TarifaService
) {

    fun emitirBoletaMensual(rutCliente: String, anio: Int, mes: Int): Boleta {
        val cliente = clientes.obtenerPorRut(rutCliente)
            ?: throw Exception("Cliente no encontrado")

        val consumo = calcularKwhClienteMes(rutCliente, anio, mes)

        val tarifa = tarifas.tarifaPara(cliente)

        val detalle = tarifa.calcular(consumo)

        val boleta = Boleta(
            idCliente = rutCliente,
            anio = anio,
            mes = mes,
            kwhTotal = consumo,
            detalle = detalle,
            estado = EstadoBoleta.EMITIDA
        )

        boletas.guardar(boleta)
        return boleta
    }

    /** Calcula el consumo mensual total (kWh) del cliente usando las lecturas registradas. */
    fun calcularKwhClienteMes(rutCliente: String, anio: Int, mes: Int): Double {

        // Obtener todas las lecturas del cliente en ese mes
        val lecturasCliente = lecturas.listarPorMedidorMes(rutCliente, anio, mes)

        // Si no hay lecturas, consumo = 0
        if (lecturasCliente.isEmpty()) return 0.0

        // Sumar los kWh leidos de todas las lecturas
        return lecturasCliente.sumOf { it.kwhLeidos }
    }

    /** Genera el PDF de las boletas emitidas del cliente en ese mes y año */
    fun exportarPdfClienteMes(
        rutCliente: String,
        anio: Int,
        mes: Int,
        pdf: PdfService
    ): ByteArray {

        val boletasCliente = boletas.listarPorCliente(rutCliente)
            .filter { it.anio == anio && it.mes == mes }

        val cliente = clientes.obtenerPorRut(rutCliente)
            ?: throw Exception("Cliente no encontrado")

        val mapaClientes = mapOf(rutCliente to cliente)
        return pdf.generarBoletasPdf(boletasCliente, mapaClientes)
    }
}
