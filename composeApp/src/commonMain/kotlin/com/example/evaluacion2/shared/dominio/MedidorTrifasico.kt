package com.example.evaluacion2.shared.dominio

class MedidorTrifasico (

    codigo: String,
    direccionSuministro: String,
    activo: Boolean,
    val potenciaMaxKw: Double,
    val factorPotencia: Double
) : Medidor(codigo, direccionSuministro, activo) {

    override fun tipo(): String = "Trifásico"
}
//función tipo que retorna "Trifásico", no se que podria necesitar esta funcion, por eso la dejo asi, mi tio la deja asi tmbn