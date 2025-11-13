package com.example.evaluacion2.shared.persistencia

// Declaración expect para multiplataforma
// Cada plataforma debe proporcionar su propia implementación
expect class StorageDriver() {
    fun put(key: String, value: ByteArray): Boolean

    fun get(key: String): ByteArray?

    fun keys(prefix: String): List<String>

    fun remove(key: String): Boolean
}
