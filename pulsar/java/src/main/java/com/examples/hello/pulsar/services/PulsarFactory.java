package com.examples.hello.pulsar.services;

import lombok.NonNull;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;

public interface PulsarFactory {
  String getTenant();

  String getNamespace();

  PulsarClient client() throws PulsarClientException;

  PulsarAdmin admin() throws PulsarClientException;

  <T> Producer<T> producerOf(@NonNull final String name, @NonNull final String topic, @NonNull final Class<T> pojo);

  <T> Consumer<T> consumerOf(@NonNull final String name, @NonNull final String topic, @NonNull final Class<T> pojo);
}
