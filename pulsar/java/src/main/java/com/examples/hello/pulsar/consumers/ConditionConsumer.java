package com.examples.hello.pulsar.consumers;

import com.examples.hello.pulsar.weather.conditions.Condition;

public interface ConditionConsumer extends MessageConsumer<Condition> {
}

