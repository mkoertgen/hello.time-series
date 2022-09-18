package com.example.hello.pulsar

import com.example.hello.pulsar.services.PulsarFactory.Companion.exclusive
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ConsumerTest : PulsarTest() {
  private val stage = "dev"
  private val topic = "conditions"

  @Test
  fun shouldProduceAndConsume() {
    container.factory(namespace = stage).use { factory ->
      log.info("Connected to pulsar: {}/{}", factory.tenant, factory.namespace)

      factory.ensureTopic(topic)

      val expected = Condition.random()

      val consumer = factory.weatherConsumer(topic)
        .exclusive().subscribe()

      val id = factory.weatherProducer(topic)
        .create().use { it.send(expected) }

      consumer.use {
        val msg = it.receive()
        assertThat(msg.messageId).isEqualTo(id)
        val actual = msg.value
        assertThat(actual).isEqualTo(expected)
      }
    }
  }
}
