package com.example.evaluacion2.shared.dominio

interface Tarifa {
    fun nombre (): String{
        TODO()
    }
    fun calcular(kwh: Double): TarifaDetalle{
        TODO()
    }
}