package com.example.hello.pulsar

import com.example.hello.pulsar.services.PulsarFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testcontainers.containers.PulsarContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
abstract class PulsarTest {
  companion object {
    val log: Logger = LoggerFactory.getLogger(PulsarTest::class.java)

    private const val PULSAR_VERSION = "2.10.1"
    @Container
    val container: PulsarContainer = PulsarContainer(DockerImageName.parse("apachepulsar/pulsar:$PULSAR_VERSION"))

    fun PulsarContainer.factory(tenant: String = "public", namespace: String = "dev") = PulsarFactory(
      this.pulsarBrokerUrl, this.httpServiceUrl, tenant, namespace)
  }
}

