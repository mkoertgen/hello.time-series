/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.examples.hello.pulsar;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.impl.schema.AvroSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static boolean shuttingDown = false;

    private static String PULSAR_HOST = getProp("PULSAR_HOST", "localhost");
    private static String PULSAR_TOPIC = getProp("PULSAR_TOPIC", "conditions");
    private enum AppMode { consumer, producer, function }
    private static AppMode APP_MODE = AppMode.valueOf(getProp("APP_MODE", AppMode.function.toString()));

    public static void main(String[] args) throws PulsarClientException, InterruptedException {
      addShutdownHook();

      switch(APP_MODE) {
        case consumer: consume(); break;
        case producer: produce(); break;
        case function: break;
      }
    }

  private static void addShutdownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        Thread.sleep(200);
        System.out.println("Shutting down ...");
        shuttingDown = true;
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        e.printStackTrace();
      }
    }));
  }

  public static void consume() throws PulsarClientException {
      var client = createClient(PULSAR_HOST);
      var subscriptionName = getProp("PULSAR_SUBSCRIPTION_NAME", "my-java-sub");
      var consumer = client.newConsumer(AvroSchema.of(Condition.class))
        .topic(PULSAR_TOPIC)
        .subscriptionName(subscriptionName)
        .subscribe();

      while (!shuttingDown) {
        var msg = consumer.receive();
        try {
          var condition = msg.getValue();
          var ts = Instant.ofEpochMilli(msg.getPublishTime());
          var id = msg.getMessageId();
          var t = condition.getTemperature();
          var h = condition.getHumidity();
          logger.info("Received msg(time={} id={}), condition(T={} H={})", ts, id, t, h);

          consumer.acknowledge(msg);
        } catch (Exception e) {
          logger.error("Could not receive message", e);
          consumer.negativeAcknowledge(msg);
        }
      }
      client.close();
    }

    public static void produce() throws PulsarClientException, InterruptedException {
      var client = createClient(PULSAR_HOST);
      var producer = client.newProducer(AvroSchema.of(Condition.class))
        .topic(PULSAR_TOPIC)
        .create();
      var condition = new Condition();
      var rnd = new Random();
      var sleepMs = Integer.parseInt(getProp("PULSAR_INTERVAL_MS", "0"));
      while (!shuttingDown) {
        var t = rnd.nextFloat() * 40;
        var h = rnd.nextFloat() * 100;
        condition.setTemperature(t);
        condition.setHumidity(h);
        producer.send(condition);
        logger.info("Sent condition(T={} H={})", t, h);
        if (sleepMs > 0)
          Thread.sleep(sleepMs);
      }
      client.close();
    }

    public static PulsarClient createClient(String pulsarHost) throws PulsarClientException {
      var serviceUrl = String.format("pulsar://%s:6650", pulsarHost);
      return PulsarClient.builder()
        .serviceUrl(serviceUrl)
        .build();
    }

    public static String getProp(String name, String defaultValue)
    {
      return Optional.ofNullable(System.getenv(name))
          .orElse(System.getProperty(name, defaultValue));
    }
}
