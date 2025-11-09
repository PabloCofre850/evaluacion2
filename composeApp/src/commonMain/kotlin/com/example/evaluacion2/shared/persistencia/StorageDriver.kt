package com.example.evaluacion2.shared.persistencia


class StorageDriver {

    // Diccionario en memoria: clave → contenido
    private val data = mutableMapOf<String, ByteArray>()


    fun put(key: String, value: ByteArray): Boolean {
        data[key] = value
        return true
    }

    fun get(key: String): ByteArray? = data[key]


    fun keys(prefix: String): List<String> =
        data.keys.filter { it.startsWith(prefix) }


    fun remove(key: String): Boolean =
        data.remove(key) != null
}
