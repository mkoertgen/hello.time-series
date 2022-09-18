package com.example.hello.pulsar.services

import org.slf4j.LoggerFactory

abstract class PulsarRunner : AutoCloseable, PulsarRunnable {
    override var isRunning = false

    override fun stop() {
      isRunning = false
    }

    override fun run() {
        if (isRunning) throw UnsupportedOperationException("Already started")
        isRunning = true
        while (isRunning) {
            try {
                callOnce()
            } catch (e: Exception) {
                log.warn("Could not process", e)
            }
        }
    }

    protected abstract fun callOnce()

    companion object {
        private val log = LoggerFactory.getLogger(PulsarRunner::class.java)
    }
}
