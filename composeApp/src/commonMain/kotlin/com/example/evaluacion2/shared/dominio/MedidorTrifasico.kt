package com.example.evaluacion2.shared.dominio

class MedidorTrifasico (

    codigo: String,
    direccionSuministro: String,
    activo: Boolean,

    val potenciaMaxKw: Double,
    val factorPotencia: Double

) : Medidor(codigo, direccionSuministro, activo) {

    override fun tipo(): String = "Trifasico"

}