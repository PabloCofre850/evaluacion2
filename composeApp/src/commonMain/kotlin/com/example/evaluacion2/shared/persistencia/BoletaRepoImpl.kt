package com.example.evaluacion2.shared.persistencia

import com.example.evaluacion2.shared.dominio.Boleta

// Repositorio de boleta -> Guarda y lista boletas en el sistema de persistencia
class BoletaRepoImpl(private val persistencia: PersistenciaDatos) : BoletaRepositorio {

    override fun guardar(b: Boleta): Boleta {
        // Generamos una key, para identificar una boleta
        persistencia.save(
            "boleta_${b.idCliente}_${b.anio}_${b.mes}",
            b.toBytes()
            // Convierte la boleta en un arreglo de bytes
        )
        return b
        // Retornamos la boleta guardada
    }

    // Obtenemos una boleta en especifico desde la persistencia, usando rut, año y mes
    override fun obtener(rut: String, anio: Int, mes: Int): Boleta? {
        return persistencia.read("boleta_${rut}_${anio}_${mes}")
    }
    // Lista TODAS las boletas pertenecientes a un cliente en particular
    override fun listarPorCliente(rut: String): List<Boleta> {
        val keys = persistencia.list("boleta_${rut}_")
        // Busca todas las "keys" que comiencen con "boleta_<rut del cliente>"
        return keys.mapNotNull { persistencia.read(it) }
        // Descarta todas las que sean nulas
    }
}


// ----- Funcion de extension privada -----
// Convierte un objeto Boleta a un arreglo de bytes (ByteArray)
// para poder guardarlo en la persistencia como texto.
private fun Boleta.toBytes(): ByteArray {
    val datos = """
        RUT: $idCliente
        Año: $anio
        Mes: $mes
        kWh: $kwhTotal
        Estado: $estado
    """.trimIndent()
    // Convierte ese texto en un arreglo de bytes
    return datos.encodeToByteArray()
}

