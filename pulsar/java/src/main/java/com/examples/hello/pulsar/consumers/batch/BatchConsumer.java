package com.examples.hello.pulsar.consumers.batch;

import org.apache.pulsar.client.api.Message;

import java.util.List;
import java.util.function.Consumer;

public interface BatchConsumer<T> extends Consumer<List<Message<T>>> {
}
