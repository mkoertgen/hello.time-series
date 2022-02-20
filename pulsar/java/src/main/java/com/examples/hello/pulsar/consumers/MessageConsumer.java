package com.examples.hello.pulsar.consumers;

import org.apache.pulsar.client.api.Message;

import java.util.function.Consumer;

public interface MessageConsumer<T> extends Consumer<Message<T>> {
}
