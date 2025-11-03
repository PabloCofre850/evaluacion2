package com.example.evaluacion2.shared.persistencia

class PersistenciaDatos (
    var driver: StorageDriver
){
        fun save(key: String, bytes: ByteArray): Boolean{
            TODO(
                "Not yet implemented"
            )
        }
        fun read (key: String): ByteArray?{
            TODO(
                "Not yet implemented"
            )
        }
    fun list(prefix: String): List<String>{
        TODO(
            "Not yet implemented"
        )
    }

    fun delete (key: String): Boolean{
        TODO(
            "Not yet implemented"
        )
    }

}