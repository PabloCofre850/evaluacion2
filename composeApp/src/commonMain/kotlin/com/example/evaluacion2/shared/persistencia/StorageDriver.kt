package com.example.evaluacion2.shared.persistencia

import com.example.evaluacion2.shared.dominio.Boleta

// Clase de base de almacenamiento (temporal en memoria)
// Se puede reemplazar mas adelante por una versión que use archivos o base de datos.

class StorageDriver {

    private val data = mutableMapOf<String, ByteArray>()

    // Diccionario en memoria

    fun put(key: String, value: ByteArray): Boolean {
        data[key] = value
        return true
    }

    // Guarda un valor (como bytes) con una clave única

    // Obtiene un valor (ByteArray) si existe
    fun get(key: String): ByteArray? {
        return data[key]
    }

    // Recupera los bytes guardados para una clave,
    // retorna null si no existe

    fun keys(prefix: String = ""): List<String> =
        data.keys.filter { it.startsWith(prefix) }

    // Lista todas las claves que comienzan con cierto prefijo,
    // ej: "boleta" devolvería todas las boletas

    fun remove(key: String): Boolean =
        data.remove(key) != null

    // Elimina un dato por clave.
    // Retorna true si se eliminó algo.

}
