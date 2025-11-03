package com.example.evaluacion2.shared.persistencia

import com.example.evaluacion2.shared.dominio.Boleta

interface BoletaRepositorio {
    fun guardar(b: Boleta): Boleta
    fun obtener(rut: String, anio: Int, mes: Int): Boleta?
    fun listarPorCliente(rut: String): List<Boleta>
}