package com.examples.hello.pulsar.producers.batch;

import com.examples.hello.pulsar.consumers.batch.BatchConsumer;
import com.examples.hello.pulsar.weather.conditions.Condition;

import java.util.List;
import java.util.concurrent.Callable;

public interface BatchProducer<T> extends Callable<List<T>> {
}

