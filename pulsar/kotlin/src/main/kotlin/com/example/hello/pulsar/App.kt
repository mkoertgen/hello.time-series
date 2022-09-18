package com.example.hello.pulsar

import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.impl.schema.AvroSchema
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Instant
import kotlin.random.Random


@Deprecated("Use Micronaut Application  instead")
class App {
  private val log: Logger = LoggerFactory.getLogger(App::class.java)
  private val pulsarHost = getProp("PULSAR_HOST", "localhost")
  private val topic = getProp("PULSAR_TOPIC", "conditions")
  private val appMode = AppMode.valueOf(getProp("APP_MODE", AppMode.Function.toString()))

  fun main() {
    addShutdownHook()

    when (appMode) {
      AppMode.Consumer -> consume()
      AppMode.Producer -> produce()
      else -> return
    }
  }

  private enum class AppMode { Consumer, Producer, Function }

  private fun consume() {
    val client = createClient()
    val subscriptionName = getProp("PULSAR_SUBSCRIPTION_NAME", "my-java-sub")
    val consumer = client
      .newConsumer(AvroSchema.of(Condition::class.java))
      .topic(topic)
      .subscriptionName(subscriptionName)
      .subscribe()

    while (!shuttingDown) {
      val msg = consumer.receive()
      try {
        val condition = msg.value
        val ts = Instant.ofEpochMilli(msg.publishTime)
        val id = msg.messageId
        val t = condition.temperature
        val h = condition.humidity
        log.info("Received msg(time={} id={}), condition(T={} H={})", ts, id, t, h)
        consumer.acknowledge(msg)
      } catch (e: Exception) {
        log.error("Could not receive message", e)
        consumer.negativeAcknowledge(msg)
      }
    }
    client.close()
  }

  private fun produce() {
    val client = createClient()
    val producer = client
      .newProducer(AvroSchema.of(Condition::class.java))
      .topic(topic)
      .create()
    val condition = Condition()
    val sleepMs = getProp("PULSAR_INTERVAL_MS", "0").toLong()
    while (!shuttingDown) {
      condition.temperature = Random.nextFloat() * 40
      condition.humidity = Random.nextFloat() * 100
      producer.send(condition)
      log.info("Sent condition(T={} H={})", condition.temperature, condition.humidity)
      if (sleepMs > 0) Thread.sleep(sleepMs)
    }
    client.close()
  }


  private fun createClient(): PulsarClient {
    val serviceUrl = "pulsar://$pulsarHost:6650"
    return PulsarClient.builder()
      .serviceUrl(serviceUrl)
      .build()
  }

  private fun getProp(name: String, defaultValue: String): String {
    return System.getenv(name) ?: System.getProperty(name, defaultValue)
  }

  private var shuttingDown = false
  private fun addShutdownHook() {
    Runtime.getRuntime().addShutdownHook(Thread {
      try {
        Thread.sleep(200)
        println("Shutting down ...")
        shuttingDown = true
      } catch (e: InterruptedException) {
        Thread.currentThread().interrupt()
        e.printStackTrace()
      }
    })
  }
}
