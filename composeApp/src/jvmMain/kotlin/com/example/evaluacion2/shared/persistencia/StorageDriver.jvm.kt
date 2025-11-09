package com.example.evaluacion2.shared.persistencia

import java.io.File

// Implementación específica para JVM
actual class StorageDriver {

    // Directorio base donde se almacenarán los archivos
    private val storageDir = File(System.getProperty("user.home"), ".evaluacion2_data").apply {
        if (!exists()) {
            mkdirs()
            println("Directorio de almacenamiento creado en: ${absolutePath}")
        } else {
            println("Usando directorio de almacenamiento: ${absolutePath}")
        }
    }

    // Convierte una clave en un archivo
    private fun keyToFile(key: String): File = File(storageDir, "$key.csv")

    actual fun put(key: String, value: ByteArray): Boolean {
        return try {
            val file = keyToFile(key)
            file.writeBytes(value)
            println("Archivo guardado: ${file.absolutePath}")
            true
        } catch (e: Exception) {
            println("Error al escribir archivo [$key]: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    actual fun get(key: String): ByteArray? {
        return try {
            val file = keyToFile(key)
            if (file.exists() && file.isFile) {
                val data = file.readBytes()
                println("Archivo leído: ${file.absolutePath}")
                data
            } else {
                println("Archivo no existe: ${file.absolutePath}")
                null
            }
        } catch (e: Exception) {
            println("Error al leer archivo [$key]: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    actual fun keys(prefix: String): List<String> {
        return try {
            val files = storageDir.listFiles()
                ?.filter { it.isFile && it.name.endsWith(".csv") }
                ?.map { it.nameWithoutExtension }
                ?.filter { it.startsWith(prefix) }
                ?: emptyList()
            println("Archivos encontrados con prefijo '$prefix': ${files.size}")
            files
        } catch (e: Exception) {
            println("Error al listar archivos con prefijo [$prefix]: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    actual fun remove(key: String): Boolean {
        return try {
            val file = keyToFile(key)
            if (file.exists()) {
                val deleted = file.delete()
                println("Archivo eliminado: ${file.absolutePath} - Éxito: $deleted")
                deleted
            } else {
                println("Archivo a eliminar no existe: ${file.absolutePath}")
                false
            }
        } catch (e: Exception) {
            println("Error al eliminar archivo [$key]: ${e.message}")
            e.printStackTrace()
            false
        }
    }
}
