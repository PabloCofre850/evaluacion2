package com.example.evaluacion2.shared.dominio

import kotlinx.serialization.Serializable

// Representa una tarifa electrica generica.
// "sealed interface" nos permite tener subtipos específicos (Residencial o Comercial)
// y a la vez usar polimorfismo al calcular las tarifas totales.

@Serializable
sealed interface Tarifa {

    val nombre: String

    fun calcular(kwh: Double): TarifaDetalle


}
