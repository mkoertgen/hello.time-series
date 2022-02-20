package com.examples.hello.pulsar.consumers.batch;

import com.examples.hello.pulsar.services.PulsarRunner;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.pulsar.client.api.Consumer;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@RequiredArgsConstructor
public class BatchConsumerRunner<T> extends PulsarRunner implements BatchConsumerRunnable {
  private final Consumer<T> consumer;
  private final BatchConsumer<T> sink;

  @Override
  @SneakyThrows
  protected void callOnce() {
    val messages = consumer.batchReceive();
    if (messages.size() == 0) return;
    log.debug("Received batch (size={})", messages.size());
    val msg = StreamSupport.stream(messages.spliterator(), false).collect(Collectors.toList());
    sink.accept(msg);
    consumer.acknowledge(messages);
    log.debug("Processed batch (size={})", messages.size());
  }

  @Override
  public void close() throws Exception {
    stop();
    consumer.close();
  }
}
