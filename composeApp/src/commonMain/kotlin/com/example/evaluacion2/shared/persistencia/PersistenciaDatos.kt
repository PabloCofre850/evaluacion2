package com.example.evaluacion2.shared.persistencia

// Clase encargada de manejar la persistencia genérica de datos. Usa un StorageDriver para guardar, leer, listar y eliminar registros
// identificados por una clave única (key).

class PersistenciaDatos(private val driver: StorageDriver) {

    // Guarda un valor binario asociado a una clave. Devuelve true si la operación fue exitosa.

    fun save(key: String, bytes: ByteArray): Boolean {
        return try {
            driver.put(key, bytes)
        } catch (e: Exception) {
            println("Error al guardar [$key]: ${e.message}")
            false
        }
    }

    // Lee los bytes almacenados bajo una clave. Devuelve null si no existe o hay error.

    fun read(key: String): ByteArray? {
        return try {
            driver.get(key)
        } catch (e: Exception) {
            println("Error al leer [$key]: ${e.message}")
            null
        }
    }

    // Lista todas las claves guardadas con el prefijo indicado.
    fun list(prefix: String): List<String> {
        return try {
            driver.keys(prefix)
        } catch (e: Exception) {
            println("Error al listar con prefijo [$prefix]: ${e.message}")
            emptyList()
        }
    }

    //Elimina el registro asociado a una clave.Devuelve true si se eliminó correctamente.

    fun delete(key: String): Boolean {
        return try {
            driver.remove(key)
        } catch (e: Exception) {
            println("Error al eliminar [$key]: ${e.message}")
            false
        }
    }
}
