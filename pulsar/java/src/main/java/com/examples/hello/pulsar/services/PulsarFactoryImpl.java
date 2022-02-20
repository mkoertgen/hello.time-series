package com.examples.hello.pulsar.services;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.impl.schema.AvroSchema;
import org.apache.pulsar.common.policies.data.TenantInfo;

@Factory
@Singleton
@Slf4j
public class PulsarFactoryImpl implements AutoCloseable, PulsarFactory {
  @Value("${pulsar.client.url:`pulsar://localhost:6650`}")
  String clientUrl;
  @Value("${pulsar.admin.url:`http://localhost:8080`}")
  String adminUrl;
  @Value("${pulsar.tenant:public}")
  @Getter
  String tenant;
  @Value("${pulsar.namespace:dev}")
  @Getter
  String namespace;
  @Value("${pulsar.topics.partitions:-1}")
  int partitions;

  private PulsarClient client;
  private PulsarAdmin admin;

  public String fqn(String value) {
    return tenant + "/" + namespace + "/" + value;
  }

  @Override
  @Singleton
  public PulsarClient client() throws PulsarClientException {
    if (client == null) client = PulsarClient.builder().serviceUrl(clientUrl).build();
    return client;
  }

  @Override
  @Singleton
  public PulsarAdmin admin() throws PulsarClientException {
    if (admin == null) admin = PulsarAdmin.builder().serviceHttpUrl(adminUrl).build();
    return admin;
  }

  @Override
  public void close() throws Exception {
    if (client != null) {
      client.close();
      client = null;
    }
    if (admin != null) {
      admin.close();
      admin = null;
    }
  }

  @Override
  @SneakyThrows
  public <T> Consumer<T> consumerOf(@NonNull String name, @NonNull String topic, @NonNull final Class<T> pojo) {
    ensureTopic(topic, partitions);
    return client().newConsumer(AvroSchema.of(pojo))
      .topic(fqn(topic))
      .subscriptionName(fqn(name))
      .subscriptionMode(SubscriptionMode.Durable)
      .subscriptionType(SubscriptionType.Shared)
      .subscribe();
  }

  @Override
  @SneakyThrows
  public <T> Producer<T> producerOf(@NonNull String name, @NonNull String topic, @NonNull final Class<T> pojo) {
    ensureTopic(topic, partitions);
    return client().newProducer(AvroSchema.of(pojo))
      .topic(fqn(topic))
      .producerName(name)
      .create();
  }

  @SneakyThrows
  private void ensureTopic(@NonNull String topic, int partitions) {
    ensureNamespace();
    val topics = admin().topics();
    val topicFqn = fqn(topic);
    val nsFqn = tenant + "/" + this.namespace;
    val isPartitioned = partitions > 0;
    if (!isPartitioned) {
      val existing = topics.getList(nsFqn);
      if (existing.contains("persistent://" + topicFqn)) return;
      topics.createNonPartitionedTopic(topicFqn);
      log.info("Created topic '{}'", topicFqn);
    } else {
      val existing = topics.getPartitionedTopicList(nsFqn);
      if (existing.contains(topic)) return;
      topics.createPartitionedTopic(topicFqn, partitions);
      log.info("Created topic '{}' ({} partitions)", topicFqn, partitions);
    }
  }

  @SneakyThrows
  private void ensureNamespace() {
    ensureTenant();
    val namespaces = admin().namespaces();
    val nsFqn = tenant + "/" + namespace;
    if (namespaces.getNamespaces(tenant).contains(nsFqn)) return;
    namespaces.createNamespace(nsFqn);
    log.info("Created namespace '{}/{}'", tenant, namespace);
  }

  @SneakyThrows
  private void ensureTenant() {
    if (admin().tenants().getTenants().contains(tenant)) return;
    log.warn("No such tenant '{}'", tenant);
    admin().tenants().createTenant(tenant, TenantInfo.builder().build());
    log.info("Created tenant '{}'", tenant);
  }
}
