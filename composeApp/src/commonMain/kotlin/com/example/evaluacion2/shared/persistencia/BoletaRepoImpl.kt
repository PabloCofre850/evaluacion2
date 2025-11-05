package com.example.evaluacion2.shared.persistencia

import com.example.evaluacion2.shared.dominio.*

class BoletaRepoImpl(private val persistencia: PersistenciaDatos) : BoletaRepositorio {

    override fun guardar(b: Boleta): Boleta {
        // Guarda la boleta en persistencia como bytes
        persistencia.save(
            key = "boleta_${b.idCliente}_${b.anio}_${b.mes}",
            bytes = b.toBytes()
        )
        return b
    }

    override fun obtener(rut: String, anio: Int, mes: Int): Boleta? {
        // Lee los bytes almacenados
        val bytes = persistencia.read("boleta_${rut}_${anio}_${mes}")
        // Si hay datos, los convierte a Boleta
        return bytes?.toBoleta()
    }

    override fun listarPorCliente(rut: String): List<Boleta> {
        val keys = persistencia.list("boleta_${rut}_")
        // Lee todas las claves y convierte los bytes a boletas
        return keys.mapNotNull {
            persistencia.read(it)?.toBoleta()
        }
    }
}

/* ------------------------------------------------------------
   FUNCIONES DE EXTENSION: para convertir entre texto y bytes
   ------------------------------------------------------------ */

// Convierte una Boleta a bytes (para guardar)
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

// Convierte bytes en una instancia de Boleta (al leer)
private fun ByteArray.toBoleta(): Boleta {
    val texto = decodeToString()
    val lineas = texto.lines().associate {
        val partes = it.split(":").map { p -> p.trim() }
        if (partes.size == 2) partes[0] to partes[1] else "" to ""
    }

    return Boleta(
        idCliente = lineas["RUT"] ?: "",
        anio = lineas["Año"]?.toIntOrNull() ?: 0,
        mes = lineas["Mes"]?.toIntOrNull() ?: 0,
        kwhTotal = lineas["kWh"]?.toDoubleOrNull() ?: 0.0,
        detalle = TarifaDetalle(" N/A ", 0.0, 0.0, 0.0), // se deja vacío
        estado = try {
            EstadoBoleta.valueOf(lineas["Estado"] ?: "EMITIDA")
        } catch (_: Exception) {
            EstadoBoleta.EMITIDA
        }
    )
}
