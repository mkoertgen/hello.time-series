package com.examples.hello.pulsar.producers;

import java.util.concurrent.Callable;

public interface MessageProducer<T> extends Callable<T> {
}
