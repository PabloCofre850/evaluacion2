package com.example.evaluacion2.shared.dominio

class MedidorMonofasico (
    codigo: String,
    direccionSuministro: String,
    activo: Boolean,

    val potenciaMaxKw: Double

): Medidor(codigo, direccionSuministro, activo) {

    override fun tipo(): String = "Monofasico"
}
