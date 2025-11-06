package com.example.evaluacion2.shared.persistencia

import com.example.evaluacion2.shared.dominio.Boleta

class PersistenciaDatos(private val driver: StorageDriver) {

    fun save(key: String, bytes: ByteArray): Boolean =
        driver.put(key, bytes)


    fun read(key: String): ByteArray? =
        driver.get(key)


    fun list(prefix: String): List<String> =
        driver.keys(prefix)


    fun delete(key: String): Boolean =
        driver.remove(key)

}
