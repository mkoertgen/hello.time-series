package com.example.hello.pulsar

import io.micronaut.context.annotation.Factory
import jakarta.inject.Named
import jakarta.inject.Singleton
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Factory
class PulsarExecutors {
    @Singleton
    @Named(PRODUCER)
    fun producer(): ExecutorService {
        return Executors.newFixedThreadPool(5)
    }

    @Singleton
    @Named(CONSUMER)
    fun consumer(): ExecutorService {
        return Executors.newFixedThreadPool(5)
    }

    @Singleton
    @Named(FUNCTIONS)
    fun functions(): ExecutorService {
        return Executors.newFixedThreadPool(5)
    }

    companion object {
        const val CONSUMER = "consumer"
        const val PRODUCER = "producer"
        const val FUNCTIONS = "functions"
    }
}
