package com.examples.hello.pulsar.functions;

import com.examples.hello.pulsar.services.PulsarRunner;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

@Slf4j
@Builder
public class FunctionRunner<I, O> extends PulsarRunner implements FunctionRunnable {
  private final Consumer<I> consumer;
  private final Producer<O> producer;
  private final Function<I, O> function;
  private final Context context;

  @SneakyThrows
  @Override
  protected void callOnce() {
    val msg = consumer.receive();
    log.debug("Received msg(time={} id={})", msg.getEventTime(), msg.getMessageId());

    val input = msg.getValue();
    val output = function.process(input, context);
    log.debug("Processed msg(time={} id={}), input={}, output={})", msg.getEventTime(), msg.getMessageId(), input, output);

    val id = producer.send(output);
    log.debug("Sent msg(id={})", id);

    consumer.acknowledge(msg);
  }

  @SneakyThrows
  @Override
  public void close() {
    consumer.close();
    producer.close();
  }
}
