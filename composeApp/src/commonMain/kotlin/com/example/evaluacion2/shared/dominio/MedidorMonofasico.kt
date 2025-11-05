package com.example.evaluacion2.shared.dominio

class MedidorMonofasico (
    codigo: String,
    direccionSuministro: String,
    activo: Boolean,
    val potenciaMaxKw: Double
): Medidor(codigo, direccionSuministro, activo) {
    override fun tipo(): String = "Monofasico"
}
//función tipo que retorna "Monofasico", no se que podria necesitar esta funcion, por eso la dejo asi, mi tio la deja asi tmbn
