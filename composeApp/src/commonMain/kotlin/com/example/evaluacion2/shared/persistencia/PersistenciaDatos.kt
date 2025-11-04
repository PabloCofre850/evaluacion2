package com.example.evaluacion2.shared.persistencia

import com.example.evaluacion2.shared.dominio.Boleta

class PersistenciaDatos(private val driver: StorageDriver) {

    fun save(key: String, bytes: ByteArray): Boolean {
        return driver.put(key, bytes)
    }

    fun read(key: String): Boleta? {
        return driver.get(key)
    }

    fun list(prefix: String): List<String> {
        return driver.keys(prefix)
    }

    fun delete(key: String): Boolean {
        return driver.remove(key)
    }
}
