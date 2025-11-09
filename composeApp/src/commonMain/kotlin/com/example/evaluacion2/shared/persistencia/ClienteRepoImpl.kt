package com.example.evaluacion2.shared.persistencia

import com.example.evaluacion2.shared.dominio.Cliente
import com.example.evaluacion2.shared.dominio.EstadoCliente

class ClienteRepoImpl(private val persistencia: PersistenciaDatos) : ClienteRepositorio {

    private fun key(rut: String) = "cliente_$rut"

    private fun toCsv(c: Cliente): String = listOf(
        c.run,                       // rut
        c.nombre,
        c.email,
        c.direccionFacturacion,
        c.estado.name,
        c.tipo
    ).joinToString(";")

    private fun fromCsv(csv: String): Cliente? =
        csv.split(";")
            .takeIf { it.size >= 6 }
            ?.let { p ->
                Cliente(
                    run = p[0],
                    nombre = p[1],
                    email = p[2],
                    direccionFacturacion = p[3],
                    estado = EstadoCliente.valueOf(p[4]),
                    tipo = p[5]
                )
            }

    override fun crear(c: Cliente): Long {
        val ok = persistencia.save(key(c.run), toCsv(c).encodeToByteArray())
        return if (ok) 1L else 0L
    }

    override fun actualizar(c: Cliente): Cliente {
        persistencia.save(key(c.run), toCsv(c).encodeToByteArray())
        return c
    }

    override fun eliminar(rut: String): Boolean =
        persistencia.delete(key(rut))

    override fun obtenerPorRut(rut: String): Cliente? =
        persistencia.read(key(rut))
            ?.decodeToString()
            ?.let { fromCsv(it) }

    override fun listar(filtro: String): List<Cliente> {
        val keys = persistencia.list("cliente_")
        return keys.mapNotNull { k ->
            persistencia.read(k)?.decodeToString()?.let { fromCsv(it) }
        }.filter { filtro.isBlank() || it.rut.contains(filtro, ignoreCase = true) }
    }
}