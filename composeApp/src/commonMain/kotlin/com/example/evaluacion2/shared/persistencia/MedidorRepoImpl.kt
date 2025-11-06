package com.example.evaluacion2.shared.persistencia

import com.example.evaluacion2.shared.dominio.Medidor
import com.example.evaluacion2.shared.dominio.MedidorMonofasico
import com.example.evaluacion2.shared.dominio.MedidorTrifasico

class MedidorRepoImpl (
    private val persistenciaDatos: PersistenciaDatos)
    : MedidorRepositorio {


    override fun crear(m: Medidor, rutCliente: String): Medidor {

        val base = mutableListOf(

            m.codigo,
            rutCliente,
            m.direccionSuministro,
            m.activo.toString(),
            m.tipo(),
        )

        when (m) {
            is MedidorMonofasico -> {
                base += m.potenciaMaxKw.toString()
                base += "" // Porque factorPotencia es un dato vacio
            }

            is MedidorTrifasico -> {
                base += m.potenciaMaxKw.toString()
                base += m.factorPotencia.toString()
            } else
                -> {
                    base += ""
                    base += ""
                    // Factor potencia y potenciaMaxKw los dejamos vacio
                }
        }

        val texto = base.joinToString(" | ")

        persistenciaDatos.save("medidor_${m.codigo}", texto.encodeToByteArray())

        return m
    }

    override fun listarPorCliente(rut: String): List<Medidor> =
        persistenciaDatos.list("medidor_")
            .mapNotNull{
                key -> persistenciaDatos.read(key)
                ?.decodeToString()
                ?.split(" | ")
                ?.map(String::trim)
                ?.let {
                    partes ->
                    if (partes.size >= 6 && partes[1] == rut) {
                        reconstruir(partes)
                    } else null
            }

    }

    override fun obtenerPorCodigo(codigo: String): Medidor? =
        persistenciaDatos.read("medidor_$codigo")
            ?.decodeToString()
            ?.split(" | ")
            ?.map(String::trim)
            ?.let { partes ->
                if (partes.size >5)
                    reconstruir(partes)
                else null
    }

    override fun eliminar(codigo: String): Boolean =
        persistenciaDatos.delete("medidor_$codigo")

    private fun reconstruir(partes: List<String>): Medidor =

        when (partes[4]) {
            // Tipo switch, para que el sistema eliga el tipo de medidor
            "Monofasico" -> MedidorMonofasico(
                codigo = partes[0],
                direccionSuministro = partes[2],
                activo = partes[3].toBoolean(),
                potenciaMaxKw = partes.getOrNull(5).orEmpty().toDoubleOrNull() ?: 0.0
            )
            "Trifasico" -> MedidorTrifasico(
                codigo = partes[0],
                direccionSuministro = partes[2],
                activo = partes[3].toBoolean(),
                potenciaMaxKw = partes.getOrNull(5).orEmpty().toDoubleOrNull() ?: 0.0,
                factorPotencia = partes.getOrNull(5).orEmpty().toDoubleOrNull() ?: 0.0
            )
            else -> Medidor (
                codigo = partes[0],
                direccionSuministro = partes[2],
                activo = partes[3].toBoolean(),
            )
        }

}