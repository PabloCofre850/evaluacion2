package com.example.evaluacion2.shared.persistencia

import com.example.evaluacion2.shared.dominio.Medidor

interface MedidorRepositorio {
    fun crear(m: Medidor, rutCliente: String): Medidor
    fun listarPorCliente(rut: String): List<Medidor>
    fun obtenerPorCodigo(codigo: String): Medidor?
    fun eliminar(codigo: String): Boolean
}