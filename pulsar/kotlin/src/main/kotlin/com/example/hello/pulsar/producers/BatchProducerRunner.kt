package com.example.hello.pulsar.producers

import com.example.hello.pulsar.services.PulsarRunner
import org.apache.pulsar.client.api.Producer
import org.slf4j.LoggerFactory

class BatchProducerRunner<T>(
    private val producer: Producer<T>,
    private val source: () -> List<T>
) : PulsarRunner(), BatchProducerRunnable {
    override fun callOnce() {
        val messages = source.invoke()
        for (message in messages) producer.send(message)
        log.debug("Produced {} values (batch)", messages.size)
    }

    override fun close() {
        producer.close()
    }

  companion object {
    private val log = LoggerFactory.getLogger(BatchProducerRunner::class.java)
  }
}
