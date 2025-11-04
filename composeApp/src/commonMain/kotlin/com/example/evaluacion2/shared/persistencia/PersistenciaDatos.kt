package com.example.evaluacion2.shared.persistencia

class PersistenciaDatos(private val driver: StorageDriver) {

    fun save(key: String, bytes: ByteArray): Boolean {
        return driver.put(key, bytes)
    }

    fun read(key: String): ByteArray? {
        return driver.get(key)
    }

    fun list(prefix: String): List<String> {
        return driver.keys(prefix)
    }

    fun delete(key: String): Boolean {
        return driver.remove(key)
    }
}
