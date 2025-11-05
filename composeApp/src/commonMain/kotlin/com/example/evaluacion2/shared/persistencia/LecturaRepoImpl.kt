package com.example.evaluacion2.shared.persistencia

import com.example.evaluacion2.shared.dominio.LecturaConsumo

class LecturaRepoImpl(private val persistencia: PersistenciaDatos) : LecturaRepositorio {

    override fun registrar(l: LecturaConsumo): LecturaConsumo {
        persistencia.save(
            key = "lectura_${l.idMedidor}_${l.anio}_${l.mes}",
            bytes = l.toString().encodeToByteArray()
        )
        return l
    }



    override fun listarPorMedidorMes(idMedidor: String, anio: Int, mes: Int): List<LecturaConsumo> {
        val keyPrefix = "lectura_${idMedidor}_${anio}_${mes}"
        val keys = persistencia.list(keyPrefix)
        return keys.mapNotNull { persistencia.read(it) as LecturaConsumo? }
    }



    override fun ultimaLectura(idMedidor: String): LecturaConsumo? {
        // Busca todas las lecturas del medidor
        val keys = persistencia.list("lectura_${idMedidor}_")

        // Si no hay lecturas, retorna null
        if (keys.isEmpty()) return null

        // Obtiene la última (la que tiene el mes mas alto)
        val ultimaClave = keys
            .mapNotNull { it.substringAfter("lectura_${idMedidor}_").split("_") }
            .maxByOrNull { partes -> partes.firstOrNull()?.toIntOrNull() ?: 0 }

        return ultimaClave?.let {
            persistencia.read("lectura_${idMedidor}_${it[0]}_${it[1]}") as LecturaConsumo?
        }
    }


}