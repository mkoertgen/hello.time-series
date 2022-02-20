package com.examples.hello.pulsar.producers;

import com.examples.hello.pulsar.Condition;
import com.examples.hello.pulsar.services.PulsarFactory;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.time.Duration;
import java.util.Random;

@Factory
@Slf4j
public class ProducerFactory {
  private final Random rnd = new Random();
  @Value("${pulsar.producer.name}")
  String name;
  @Inject
  PulsarFactory factory;
  @Value("${pulsar.producer.topic}")
  String topic;
  @Value("${pulsar.producer.interval}")
  Duration interval;

  @Singleton
  public ProducerRunner<Condition> source() {
    val producer = factory.producerOf(name, topic, Condition.class);
    return new ProducerRunner<>(producer, this::produceCondition);
  }

  private Condition produceCondition() throws InterruptedException {
    if (interval.toMillis() > 0)
      Thread.sleep(interval.toMillis());
    return Condition.builder()
      .temperature(rnd.nextFloat() * 40)
      .humidity(rnd.nextFloat() * 100)
      .build();
  }
}
