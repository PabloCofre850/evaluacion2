package com.example.evaluacion2.shared.persistencia

import com.example.evaluacion2.shared.dominio.LecturaConsumo
interface LecturaRepositorio {
    fun registrar(l: LecturaConsumo): LecturaConsumo
    fun listarPorMedidorMes(idMedidor: String, anio: Int, mes: Int): List<LecturaConsumo>
    fun ultimaLectura(idMedidor: String): LecturaConsumo?
}