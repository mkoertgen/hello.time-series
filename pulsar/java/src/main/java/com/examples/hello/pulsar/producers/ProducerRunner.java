package com.examples.hello.pulsar.producers;

import com.examples.hello.pulsar.services.PulsarRunner;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.pulsar.client.api.Producer;

@Slf4j
@RequiredArgsConstructor
public class ProducerRunner<T> extends PulsarRunner implements ProducerRunnable {
  private final Producer<T> producer;
  private final MessageProducer<T> source;

  @SneakyThrows
  @Override
  protected void callOnce() {
    val value = source.call();
    val id = producer.send(value);
    log.debug("Sent value={}, id={})", value, id);
  }

  @Override
  public void close() throws Exception {
    producer.close();
  }
}

