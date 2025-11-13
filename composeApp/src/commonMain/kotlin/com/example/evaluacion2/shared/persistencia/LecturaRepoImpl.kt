package com.example.evaluacion2.shared.persistencia

import com.example.evaluacion2.shared.dominio.LecturaConsumo

class LecturaRepoImpl(
    private val persistenciaDatos: PersistenciaDatos)
    : LecturaRepositorio {

    override fun registrar(l: LecturaConsumo): LecturaConsumo {

        val key = "lectura_${l.idMedidor}_${l.anio}_${l.mes}"

        // Creamos la llave para llamar a la lectura de cada medidor

        val text = listOf(l.idMedidor, l.anio, l.mes, l.kwhLeidos)
            .joinToString(";")
        persistenciaDatos.save(key, text.encodeToByteArray())
        return l

        // Guardamos las lecturas en Persistencia de Datos

    }


    override fun listarPorMedidorMes(idMedidor: String, anio: Int, mes: Int): List<LecturaConsumo> =
        persistenciaDatos
            .list("lectura_${idMedidor}_${anio}_${mes}")
            .mapNotNull { key ->
                persistenciaDatos.read(key)
                    ?.decodeToString()
                    ?.split(";")
                    ?.takeIf { it.size == 4 }
                    ?.let { parts ->
                        LecturaConsumo(
                            idMedidor = parts[0],
                            anio = parts[1].toInt(),
                            mes = parts[2].toInt(),
                            kwhLeidos = parts[3].toDouble()
                        )
                    }
            }

    override fun ultimaLectura(idMedidor: String): LecturaConsumo? {

        val allKeys = persistenciaDatos.list("lectura_${idMedidor}_")

        val lastKey = allKeys.maxByOrNull {
            key -> key
                .substringAfterLast("_")
                .toIntOrNull() ?: 0
        } ?: return null

        return persistenciaDatos.read(lastKey)
            ?.decodeToString()
            ?.split(";")
            ?.takeIf { it.size == 4 }
            ?.let { p -> LecturaConsumo(
                p[0],
                p[1].toInt(),
                p[2].toInt(),
                p[3].toDouble()
            ) }
    }

}