package com.example.evaluacion2.shared.servicios

import com.example.evaluacion2.shared.dominio.*

class TarifaService {

    fun tarifaPara(cliente: Cliente): Tarifa {

        // Convertimos el tipo del cliente a minusculas para comparar
        val tipo = cliente.tipo.lowercase()

        return when (tipo) {
            "residencial" -> TarifaResidencial(  // REVISAR VALORES
                cargoFijo = 1200.0,
                precioKwh = 115.0,
                iva = 0.19
            )
            "comercial" -> TarifaComercial(
                cargoFijo = 2000.0,
                precioKwh = 130.0,
                iva = 0.19
            )
            else -> throw Exception("Tipo de cliente desconocido: ${cliente.tipo}")
        }
    }
}
