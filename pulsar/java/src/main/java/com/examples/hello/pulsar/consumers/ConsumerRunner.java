package com.examples.hello.pulsar.consumers;

import com.examples.hello.pulsar.services.PulsarRunner;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.pulsar.client.api.Consumer;

@Slf4j
@RequiredArgsConstructor
public class ConsumerRunner<T> extends PulsarRunner implements ConsumerRunnable {
  private final Consumer<T> consumer;
  private final MessageConsumer<T> sink;

  @Override
  @SneakyThrows
  protected void callOnce() {
    val msg = consumer.receive();
    sink.accept(msg);
    consumer.acknowledge(msg);
    log.debug("Received msg(time={} id={}), value={})", msg.getEventTime(), msg.getMessageId(), msg.getValue());
  }

  @Override
  public void close() throws Exception {
    stop();
    consumer.close();
  }
}

