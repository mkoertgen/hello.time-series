package com.examples.hello.pulsar.consumers.batch;

import com.examples.hello.pulsar.weather.conditions.Condition;

public interface ConditionBatchConsumer extends BatchConsumer<Condition> {
}
