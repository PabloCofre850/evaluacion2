package com.example.evaluacion2.shared.dominio

open class Medidor (
    val codigo: String,
    val direccionSuministro: String,
    val activo: Boolean
){

    // Metodo para obtener el tipo de medidor
    open fun tipo(): String = "Genérico"

}