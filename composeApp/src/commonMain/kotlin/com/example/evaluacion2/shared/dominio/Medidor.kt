package com.example.evaluacion2.shared.dominio

open class Medidor (
    val codigo: String,
    val direccionSuministro: String,
    var activo: Boolean
){

    // Método para obtener el tipo de medidor
    open fun tipo(): String {
        return "Genérico"
    }
}