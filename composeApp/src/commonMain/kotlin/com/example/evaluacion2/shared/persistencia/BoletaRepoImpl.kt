package com.example.evaluacion2.shared.persistencia

import com.example.evaluacion2.shared.dominio.Boleta

class BoletaRepoImpl(private val persistencia: PersistenciaDatos) : BoletaRepositorio {

    override fun guardar(b: Boleta): Boleta {
        persistencia.save(
            "boleta_${b.idCliente}_${b.anio}_${b.mes}",
            b.toBytes()
        )
        return b
    }

    override fun obtener(rut: String, anio: Int, mes: Int): Boleta? {
        return persistencia.read("boleta_${rut}_${anio}_${mes}")
    }

    override fun listarPorCliente(rut: String): List<Boleta> {
        val keys = persistencia.list("boleta_${rut}_")
        return keys.mapNotNull { persistencia.read(it) }
    }
}

private fun Boleta.toBytes(): ByteArray {
    val datos = """
        RUT: $idCliente
        Año: $anio
        Mes: $mes
        kWh: $kwhTotal
        Estado: $estado
    """.trimIndent()
    return datos.encodeToByteArray()
}

