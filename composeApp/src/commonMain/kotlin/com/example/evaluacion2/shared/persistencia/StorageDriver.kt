package com.example.evaluacion2.shared.persistencia

import java.io.File

class StorageDriver {

    // Directorio base donde se almacenarán los archivos
    private val storageDir = File(System.getProperty("user.home"), ".evaluacion2_data").apply {
        if (!exists()) {
            mkdirs()
        }
    }

    // Convierte una clave en un archivo
    private fun keyToFile(key: String): File = File(storageDir, "$key.csv")

    fun put(key: String, value: ByteArray): Boolean {
        return try {
            val file = keyToFile(key)
            file.writeBytes(value)
            true
        } catch (e: Exception) {
            println("Error al escribir archivo [$key]: ${e.message}")
            false
        }
    }

    fun get(key: String): ByteArray? {
        return try {
            val file = keyToFile(key)
            if (file.exists() && file.isFile) {
                file.readBytes()
            } else {
                null
            }
        } catch (e: Exception) {
            println("Error al leer archivo [$key]: ${e.message}")
            null
        }
    }

    fun keys(prefix: String): List<String> {
        return try {
            storageDir.listFiles()
                ?.filter { it.isFile && it.name.endsWith(".csv") }
                ?.map { it.nameWithoutExtension }
                ?.filter { it.startsWith(prefix) }
                ?: emptyList()
        } catch (e: Exception) {
            println("Error al listar archivos con prefijo [$prefix]: ${e.message}")
            emptyList()
        }
    }

    fun remove(key: String): Boolean {
        return try {
            val file = keyToFile(key)
            if (file.exists()) {
                file.delete()
            } else {
                false
            }
        } catch (e: Exception) {
            println("Error al eliminar archivo [$key]: ${e.message}")
            false
        }
    }
}
