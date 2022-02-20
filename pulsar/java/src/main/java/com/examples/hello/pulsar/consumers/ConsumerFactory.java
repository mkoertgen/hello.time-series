package com.examples.hello.pulsar.consumers;

import com.examples.hello.pulsar.Condition;
import com.examples.hello.pulsar.services.PulsarFactory;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Factory
@Slf4j
public class ConsumerFactory {
  @Inject
  private PulsarFactory factory;

  @Value("${pulsar.consumer.name}")
  String name;
  @Value("${pulsar.consumer.topic}")
  String topic;

  @Singleton
  public ConsumerRunnable consumer() {
    val consumer = factory.consumerOf(name, topic, Condition.class);
    return new ConsumerRunner<>(consumer, sink());
  }

  @Singleton
  public BatchConsumerRunnable batchConsumer() {
    val consumer = factory.consumerOf(name, topic, Condition.class);
    return new BatchConsumerRunner<>(consumer, batchSink());
  }


  @Singleton
  public ConditionSink sink() {
    return msg -> log.debug("Received condition={}", msg.getValue());
  }

  @Singleton
  public BatchConditionSink batchSink() {
    return msg -> log.debug("Received conditions batch (size={})", msg.size());
  }
}
