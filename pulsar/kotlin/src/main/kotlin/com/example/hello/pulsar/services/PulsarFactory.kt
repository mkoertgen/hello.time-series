package com.example.hello.pulsar.services

import com.example.hello.pulsar.Condition
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton
import org.apache.pulsar.client.admin.PulsarAdmin
import org.apache.pulsar.client.api.*
import org.apache.pulsar.client.impl.schema.AvroSchema
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

@Singleton
class PulsarFactory(
  @Value("\${pulsar.client.url:`pulsar://localhost:6650`}") val clientUrl: String = "pulsar://localhost:6650",
  @Value("\${pulsar.admin.url:`http://localhost:8080`}") val adminUrl: String = "http://localhost:8080",
  @Value("\${pulsar.tenant:public}") val tenant: String = "public",
  @Value("\${pulsar.namespace:dev}") val namespace: String = "dev"
) : AutoCloseable {
  private var client: PulsarClient? = null
  private var admin: PulsarAdmin? = null

  @Throws(PulsarClientException::class)
  fun client(): PulsarClient {
    if (client == null) client = PulsarClient.builder().serviceUrl(clientUrl).build()
    return client!!
  }

  @Throws(PulsarClientException::class)
  fun admin(): PulsarAdmin {
    if (admin == null) admin = PulsarAdmin.builder().serviceHttpUrl(adminUrl).build()
    return admin!!
  }

  fun <T> consumer(schema: Schema<T>): ConsumerBuilder<T> = client().newConsumer(schema)
  fun weatherConsumer(topic: String): ConsumerBuilder<Condition> = consumer(WEATHER_SCHEMA)
    .topic("$tenant/$namespace/$topic").also { this.ensureTopic(topic) }

  fun <T> producer(schema: Schema<T>): ProducerBuilder<T> = client().newProducer(schema)
  fun weatherProducer(topic: String): ProducerBuilder<Condition> = producer(WEATHER_SCHEMA)
    .topic("$tenant/$namespace/$topic")
    .also { this.ensureTopic(topic) }

//	fun <T> reader(schema: Schema<T>): ReaderBuilder<T> = client().newReader(schema)

  override fun close() {
    client?.close()
    admin?.close()
  }

  private fun ensureNamespace() {
    val ns = this.admin().namespaces()
    if (ns.getNamespaces(this.tenant).find { it.endsWith(this.namespace) } != null) return
    val fqn = "${this.tenant}/${this.namespace}"
    ns.createNamespace(fqn)
    log.info("Created namespace $fqn")
  }

  fun ensureTopic(topic: String, partitions: Int = -1) {
    ensureNamespace()
    val t = admin().topics()
    val ns = "${this.tenant}/${this.namespace}"
    val fqn = "$ns/$topic"

    if (partitions > 0) {
      if (t.getPartitionedTopicList(ns).find { it.endsWith(topic) } != null) return
      t.createPartitionedTopic(fqn, partitions)
      log.info("Created topic $fqn with $partitions partitions")
    } else {
      if (t.getList(ns).find { it.endsWith(topic) } != null) return
      t.createNonPartitionedTopic(fqn)
      log.info("Created topic $fqn")
    }
  }

  companion object {
    private val log = LoggerFactory.getLogger(PulsarFactory::class.java)

    fun <T> ConsumerBuilder<T>.batch(size: Int = 1000): ConsumerBuilder<T> =
      this.batchReceivePolicy(BatchReceivePolicy.builder()
        .maxNumMessages(size)
        .maxNumBytes(0) // disable
        .timeout(-1, TimeUnit.SECONDS) // disable, blocks!
        .build())

    fun <T> ConsumerBuilder<T>.shared(subscription: String): ConsumerBuilder<T> =
      this.subscriptionName(subscription)
        .subscriptionMode(SubscriptionMode.Durable)
        .subscriptionType(SubscriptionType.Shared)
        .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)

    fun <T> ConsumerBuilder<T>.exclusive(): ConsumerBuilder<T> =
      this.subscriptionName(subscription())
        .subscriptionMode(SubscriptionMode.NonDurable)
        .subscriptionType(SubscriptionType.Exclusive)
        .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)

//		fun <T> ReaderBuilder<T>.from(id: MessageId): Reader<T> =
//			this.startMessageId(id).startMessageIdInclusive().create()
//
//		fun <T> ReaderBuilder<T>.from(timestamp: Instant): Reader<T> {
//			val reader = this.startMessageId(MessageId.earliest).create()
//			reader.seek(timestamp.toEpochMilli())
//			return reader
//		}

    fun <T> Consumer<T>.receive(timeout: Duration): Message<T>? {
      return this.receive(timeout.inWholeMilliseconds.toInt(), TimeUnit.MILLISECONDS)
    }

    private val WEATHER_SCHEMA: Schema<Condition> = AvroSchema.of(Condition::class.java)
    private val BATCH_POLICY = BatchReceivePolicy.builder()
      .maxNumMessages(1000)
      .timeout(1, TimeUnit.SECONDS)
      .build()

    private fun subscription() = "${PulsarFactory::class.java.name}_${UUID.randomUUID()}"
  }
}
