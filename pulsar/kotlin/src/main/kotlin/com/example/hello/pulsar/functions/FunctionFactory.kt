package com.example.hello.pulsar.functions

import com.example.hello.pulsar.services.PulsarFactory
import com.example.hello.pulsar.services.PulsarFactory.Companion.shared
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.apache.pulsar.client.impl.schema.AvroSchema

@Factory
class FunctionFactory(
    @Inject private val factory: PulsarFactory,
    @Value("\${pulsar.function.name}") val functionName: String,
    @Value("\${pulsar.function.input}") val inputTopic: String,
    @Value("\${pulsar.function.output}") val outputTopic: String
) {

  @Singleton
  fun function(): FunctionRunnable {
    val func = FilterTemperatureFunction()

    val source = factory.weatherConsumer(inputTopic)
      .shared(functionName)
      .consumerName(functionName)
      .subscribe()

    val sink = factory.producer(AvroSchema.of(Float::class.java))
      .topic("${factory.tenant}/${factory.namespace}/$outputTopic")
      .also { factory.ensureTopic(outputTopic) }
      .create()

    val context = FunctionContext(
        factory.tenant, factory.namespace,
        functionName, inputTopic, outputTopic
    )
    return FunctionRunner(source, func, context, sink)
  }
}
