package com.examples.hello.pulsar.functions;

import com.examples.hello.pulsar.weather.conditions.Condition;
import com.examples.hello.pulsar.services.PulsarFactory;
import io.micrometer.core.instrument.MeterRegistry;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.SneakyThrows;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.functions.api.Context;

import java.util.Collections;

@Factory
public class FunctionFactory {
  @Inject
  PulsarFactory factory;

  @Value("${pulsar.function.name}")
  String name;
  @Value("${pulsar.function.input}")
  String input;
  @Value("${pulsar.function.output}")
  String output;
  @Inject
  MeterRegistry registry;

  @Singleton
  public FunctionRunnable service() {
    return FunctionRunner.<Condition, Float>builder()
      .consumer(factory.consumerOf(name, input, Condition.class))
      .function(new FilterTemperatureFunction())
      .producer(factory.producerOf(name, output, Float.class))
      .context(context())
      .build();
  }

  @SneakyThrows
  private Context context() {
    return SimpleFunctionContext.builder()
      .tenant(factory.getTenant())
      .namespace(factory.getNamespace())
      .pulsarAdmin(factory.admin())
      .inputTopics(Collections.singleton(input))
      .functionName(name)
      .registry(registry)
      .outputSchema(Schema.FLOAT)
      .build();
  }
}
