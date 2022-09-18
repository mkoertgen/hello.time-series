package com.example.hello.pulsar.services

interface PulsarRunnable : Runnable {
    fun stop()
    val isRunning: Boolean
}
