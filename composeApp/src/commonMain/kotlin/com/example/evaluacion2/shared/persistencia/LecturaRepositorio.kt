package com.example.evaluacion2.shared.persistencia

import com.example.evaluacion2.shared.dominio.LecturaConsumo
import com.example.evaluacion2.shared.dominio.Medidor

interface LecturaRepositorio {
    fun registrar(I: LecturaConsumo): LecturaConsumo
    fun listarPorMedidorMes(idMedidor: String, anio: Int, mes: Int): List<LecturaConsumo>
    fun ultimaLectura(idMedidor: String): LecturaConsumo?
}