package com.example.hello.pulsar.functions

import org.apache.pulsar.client.admin.PulsarAdmin
import org.apache.pulsar.client.api.ConsumerBuilder
import org.apache.pulsar.client.api.Schema
import org.apache.pulsar.client.api.TypedMessageBuilder
import org.apache.pulsar.functions.api.Context
import org.apache.pulsar.functions.api.Record
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.completedFuture

data class FunctionContext(
  private val tenant: String,
  private val namespace: String,
  private val functionName: String,
  private val input: String,
  private val output: String,
  private val userConfig: Map<String, Any> = mutableMapOf(),
  private val state: MutableMap<String, ByteBuffer> = mutableMapOf(),
  private val stateKeyCapacity: Int = 4096, // gRPC state key max size
) : Context {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(FunctionContext::class.java)
  }

  override fun getTenant(): String = tenant
  override fun getNamespace(): String = namespace
  override fun getInstanceId(): Int = 0
  override fun getNumInstances(): Int = 1
  override fun getLogger(): Logger = log

  override fun getSecret(secretName: String): String {
    TODO("Not yet implemented")
  }

  override fun putState(key: String, value: ByteBuffer) { state[key] = value }
  override fun putStateAsync(key: String, value: ByteBuffer): CompletableFuture<Void> {
    putState(key, value)
    return completedFuture(null)
  }
  override fun getState(key: String): ByteBuffer {
    if (!state.containsKey(key)) state[key] = ByteBuffer.allocate(stateKeyCapacity)
    return state[key]!!
  }
  override fun getStateAsync(key: String): CompletableFuture<ByteBuffer> = completedFuture(getState(key))
  override fun deleteState(key: String) { state.remove(key) }
  override fun deleteStateAsync(key: String): CompletableFuture<Void> {
    deleteState(key)
    return completedFuture(null)
  }
  override fun incrCounter(key: String, amount: Long) {
    getState(key).asLongBuffer()
      .apply { this.put(0, this.get(0) + amount) }
  }
  override fun incrCounterAsync(key: String, amount: Long): CompletableFuture<Void> {
    incrCounter(key, amount)
    return completedFuture(null)
  }
  override fun getCounter(key: String): Long = getState(key).getLong(0)
  override fun getCounterAsync(key: String): CompletableFuture<Long> = completedFuture(getCounter(key))

  override fun recordMetric(metricName: String, value: Double) {
    TODO("Not yet implemented")
  }

  override fun getInputTopics(): Collection<String> = listOf(input)
  override fun getOutputTopic(): String = output
  override fun getCurrentRecord(): Record<*> {
    TODO("Not yet implemented")
  }
  override fun getOutputSchemaType(): String {
    TODO("Not yet implemented")
  }
  override fun getFunctionName(): String = functionName
  override fun getFunctionId(): String = functionName
  override fun getFunctionVersion(): String {
    TODO("Not yet implemented")
  }

  override fun getUserConfigMap(): Map<String, Any> = userConfig
  override fun getUserConfigValue(key: String): Optional<Any> = userConfig[key]?.let { Optional.of(it) } ?: Optional.empty()
  override fun getUserConfigValueOrDefault(key: String, defaultValue: Any): Any = userConfig[key] ?: defaultValue

  override fun getPulsarAdmin(): PulsarAdmin {
    TODO("Not yet implemented")
  }

  @Deprecated("Deprecated in Java")
  override fun <O : Any> publish(topicName: String, value: O, schemaOrSerdeClassName: String): CompletableFuture<Void> {
    TODO("Not yet implemented")
  }

  @Deprecated("Deprecated in Java")
  override fun <O : Any> publish(topicName: String, value: O): CompletableFuture<Void> {
    TODO("Not yet implemented")
  }

  override fun <O : Any> newOutputMessage(topicName: String, schema: Schema<O>): TypedMessageBuilder<O> {
    TODO("Not yet implemented")
  }

  override fun <O : Any> newConsumerBuilder(schema: Schema<O>): ConsumerBuilder<O> {
    TODO("Not yet implemented")
  }
}
