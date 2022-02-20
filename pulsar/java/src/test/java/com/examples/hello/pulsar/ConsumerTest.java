package com.examples.hello.pulsar;

import com.examples.hello.pulsar.services.PulsarFactoryImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PulsarContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@IntegrationTest
@Testcontainers
@Slf4j
public class ConsumerTest {
  @Container
  private static final PulsarContainer PULSAR_CONTAINER = new PulsarContainer(DockerImageName.parse("apachepulsar/pulsar:2.9.1"));

  @Test
  @SneakyThrows
  @Disabled("TODO")
  public void shouldProduceAndConsume() {
    try (val factory = PulsarFactoryImpl.builder()
      .clientUrl(PULSAR_CONTAINER.getPulsarBrokerUrl())
      .adminUrl(PULSAR_CONTAINER.getHttpServiceUrl())
      .build()) {

      log.info("Connected to pulsar: {}/{}", factory.getTenant(), factory.getNamespace());

      // TODO
    }
  }
}
