package com.example.evaluacion2.shared.persistencia

import com.example.evaluacion2.shared.dominio.Cliente

interface ClienteRepositorio {
    fun crear (c: Cliente): Long
    fun actualizar (c: Cliente): Cliente
    fun eliminar (rut: String): Boolean
    fun obtenerPorRut(rut: String): Cliente?
    fun listar(filtro: String = ""): List<Cliente>
}