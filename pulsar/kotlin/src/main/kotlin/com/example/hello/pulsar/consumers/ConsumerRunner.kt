package com.example.hello.pulsar.consumers

import com.example.hello.pulsar.services.PulsarRunner
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Message
import org.slf4j.LoggerFactory

class ConsumerRunner<T>(
  private val consumer: Consumer<T>,
  private val sink: (Message<T>) -> Unit
  ) : PulsarRunner(), ConsumerRunnable {

    override fun callOnce() {
        val msg = consumer.receive()
        sink.invoke(msg)
        consumer.acknowledge(msg)
        log.debug("Received msg(time={} id={}), value={})", msg.eventTime, msg.messageId, msg.value)
    }

    override fun close() {
        stop()
        consumer.close()
    }

  companion object {
    private val log = LoggerFactory.getLogger(ConsumerRunner::class.java)
  }
}
