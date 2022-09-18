package com.example.hello.pulsar.producers

import com.example.hello.pulsar.services.PulsarRunner
import org.apache.pulsar.client.api.Producer
import org.slf4j.LoggerFactory

class ProducerRunner<T>(
  private val producer: Producer<T>,
  private val source: () -> T
) : PulsarRunner(), ProducerRunnable {

    override fun callOnce() {
        val value = source.invoke()
        val id = producer.send(value)
        log.debug("Produced value={}, id={})", value, id)
    }

    @Throws(Exception::class)
    override fun close() {
        producer.close()
    }

    companion object {
        private val log = LoggerFactory.getLogger(ProducerRunner::class.java)
    }
}
