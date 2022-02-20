package com.examples.hello.pulsar.functions;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.api.ConsumerBuilder;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.TypedMessageBuilder;
import org.apache.pulsar.functions.api.Record;
import org.slf4j.Logger;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Data
@Builder
public class SimpleFunctionContext implements org.apache.pulsar.functions.api.Context {
  private final PulsarAdmin pulsarAdmin;
  private final String tenant;
  private final String namespace;

  private final Map<String, Object> userConfigMap = new HashMap<>();
  private final Map<String, ByteBuffer> state = new HashMap<>();
  @Builder.Default
  private final MeterRegistry registry = new SimpleMeterRegistry();

  private final Collection<String> inputTopics;
  private final String outputTopic;
  private final Schema<?> outputSchema;
  private final String functionName;
  private final int instanceId = 0;
  private final int numInstances = 1;

  @Override
  public Record<?> getCurrentRecord() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getOutputSchemaType() {
    return outputSchema.getSchemaInfo().getType().name();
  }

  @Override
  public String getFunctionId() {
    return String.format("%s-%d", functionName, instanceId);
  }

  @Override
  public String getFunctionVersion() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Optional<Object> getUserConfigValue(String key) {
    return Optional.ofNullable(userConfigMap.get(key));
  }

  @Override
  public Object getUserConfigValueOrDefault(String key, Object defaultValue) {
    return userConfigMap.getOrDefault(key, defaultValue);
  }

  @Override
  public <O> CompletableFuture<Void> publish(String topicName, O object, String schemaOrSerdeClassName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <O> CompletableFuture<Void> publish(String topicName, O object) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <O> TypedMessageBuilder<O> newOutputMessage(String topicName, Schema<O> schema) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <O> ConsumerBuilder<O> newConsumerBuilder(Schema<O> schema) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Logger getLogger() { return log; }

  @Override
  public String getSecret(String secretName) {
    throw new UnsupportedOperationException();
  }

  @SneakyThrows
  @Override
  public void putState(String key, ByteBuffer value) {
    state.put(key, value);
  }

  @Override
  public CompletableFuture<Void> putStateAsync(String key, ByteBuffer value) {
    putState(key, value);
    return CompletableFuture.completedFuture(null);
  }

  @SneakyThrows
  @Override
  public ByteBuffer getState(String key) {
    return state.computeIfAbsent(key, (k) -> ByteBuffer.allocate(1024));
  }

  @Override
  public CompletableFuture<ByteBuffer> getStateAsync(String key) {
    return CompletableFuture.completedFuture(getState(key));
  }

  @SneakyThrows
  @Override
  public void deleteState(String key) {
    state.remove(key);
  }

  @Override
  public CompletableFuture<Void> deleteStateAsync(String key) {
    deleteState(key);
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public void incrCounter(String key, long amount) {
    getState(key).asLongBuffer().put(0, amount);
  }

  @Override
  public CompletableFuture<Void> incrCounterAsync(String key, long amount) {
    incrCounter(key, amount);
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public long getCounter(String key) {
    return getState(key).asLongBuffer().get(0);
  }

  @Override
  public CompletableFuture<Long> getCounterAsync(String key) {
    return CompletableFuture.completedFuture(getCounter(key));
  }

  @Override
  public void recordMetric(String metricName, double value) {
    registry.summary(metricName).record(value);
  }
}
