package com.example.evaluacion2.shared.persistencia

import com.example.evaluacion2.shared.dominio.Boleta

class BoletaRepoImpl(private val persistencia: PersistenciaDatos) : BoletaRepositorio {

    override fun guardar(b: Boleta): Boleta {

        val key = "boleta_${b.idCliente}_${b.anio}_${b.mes}"
        val csv = listOf(
            b.idCliente,
            b.anio.toString(),
            b.mes.toString(),
            b.kwhTotal.toString(),
            b.detalle.nombre,
            b.detalle.precioKwh.toString(),
            b.detalle.consumo.toString(),
            b.detalle.total.toString(),
            b.estado.name
        ).joinToString(";")

        persistencia.save(key, csv.encodeToByteArray())
        return b
    }

    override fun obtener(rut: String, anio: Int, mes: Int): Boleta? {
        val key = "boleta_${rut}_${anio}_${mes}"
        val data = persistencia.read(key)?.decodeToString() ?: return null
        val partes = data.split(";")

        return Boleta(
            idCliente = partes[0],
            anio = partes[1].toInt(),
            mes = partes[2].toInt(),
            kwhTotal = partes[3].toDouble(),
            detalle = com.example.evaluacion2.shared.dominio.TarifaDetalle(
                nombre = partes[4],
                precioKwh = partes[5].toDouble(),
                consumo = partes[6].toDouble(),
                total = partes[7].toDouble()
            ),
            estado = com.example.evaluacion2.shared.dominio.EstadoBoleta.valueOf(partes[8])
        )
    }

    override fun listarPorCliente(rut: String): List<Boleta> {
        val keys = persistencia.list("boleta_")
        return keys.mapNotNull { key ->
            val data = persistencia.read(key)?.decodeToString() ?: return@mapNotNull null
            val partes = data.split(";")
            val boleta = Boleta(
                idCliente = partes[0],
                anio = partes[1].toInt(),
                mes = partes[2].toInt(),
                kwhTotal = partes[3].toDouble(),
                detalle = com.example.evaluacion2.shared.dominio.TarifaDetalle(
                    nombre = partes[4],
                    precioKwh = partes[5].toDouble(),
                    consumo = partes[6].toDouble(),
                    total = partes[7].toDouble()
                ),
                estado = com.example.evaluacion2.shared.dominio.EstadoBoleta.valueOf(partes[8])
            )
            if (boleta.idCliente == rut) boleta else null
        }
    }
}
