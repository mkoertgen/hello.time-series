package com.example.hello.pulsar.consumers.batch

import com.example.hello.pulsar.services.PulsarRunner
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Message
import org.slf4j.LoggerFactory

class BatchConsumerRunner<T>(
  private val consumer: Consumer<T>,
  private val sink: (List<Message<T>>) -> Unit
) : PulsarRunner(), BatchConsumerRunnable {

    override fun callOnce() {
        val messages = consumer.batchReceive()
        if (messages.size() == 0) return
        log.debug("Received batch (size={})", messages.size())
        sink.invoke(messages.toList())
        consumer.acknowledge(messages)
        log.debug("Processed batch (size={})", messages.size())
    }

    override fun close() {
        stop()
        consumer.close()
    }

  companion object {
    private val log = LoggerFactory.getLogger(BatchConsumerRunner::class.java)
  }
}
