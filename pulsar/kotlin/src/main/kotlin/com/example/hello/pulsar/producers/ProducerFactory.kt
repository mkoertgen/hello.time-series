package com.example.hello.pulsar.producers

import com.example.hello.pulsar.Condition
import com.example.hello.pulsar.services.PulsarFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.time.Duration

@Factory
@Singleton
class ProducerFactory(
  @Inject private val factory: PulsarFactory,
  @Value("\${pulsar.producer.name}") val name: String,
  @Value("\${pulsar.producer.topic}") val topic: String,
  // Must be a java.time.Duration so it can be injected
  @Value("\${pulsar.producer.interval}") val interval: Duration = Duration.ZERO,
  @Value("\${pulsar.producer.batch}") val batchSize: Int = 100,
) {
  @Singleton
  fun producer(): ProducerRunnable {
    val producer = factory.weatherProducer(topic).producerName(name).create()
    return ProducerRunner(producer) {
      if (interval.toMillis() > 0) Thread.sleep(interval.toMillis())
      Condition.random()
    }
  }

  @Singleton
  fun batchProducer(): BatchProducerRunnable {
    val producer = factory.weatherProducer(topic).producerName(name).batchingMaxMessages(batchSize).create()
    return BatchProducerRunner(producer) {
      if (interval.toMillis() > 0) Thread.sleep(interval.toMillis())
      (0..batchSize).map { Condition.random() }
    }
  }
}
