package com.example.evaluacion2

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform