package com.example.evaluacion2.shared.servicios
import com.example.evaluacion2.shared.dominio.Tarifa
import com.example.evaluacion2.shared.dominio.Cliente

class TarifaService {
    fun tarifaPara(cliente: Cliente): Tarifa {
        val tipo = cliente.undercase()

        TODO()
    }
}