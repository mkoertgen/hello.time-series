package com.examples.hello.pulsar.producers;

import com.examples.hello.pulsar.weather.conditions.Condition;

public interface ConditionProducer extends MessageProducer<Condition> {
}
