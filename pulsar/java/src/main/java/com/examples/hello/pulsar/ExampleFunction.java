package com.examples.hello.pulsar;

import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.slf4j.Logger;

@SuppressWarnings("unused")
public class ExampleFunction implements Function<Condition, Float> {

  public static final String MAX_TEMPERATURE_KEY = "maxTemperature";
  public static final float HIGH_TEMPERATURE = 40f;

  @Override
  public Float process(Condition input, Context context) {
    var currTemp = input.getTemperature();

    // 1. update max temperature
    var state = context.getState(MAX_TEMPERATURE_KEY).asFloatBuffer();
    if (currTemp > state.get(0)) {
      state.put(0, currTemp);

      // publish to log topic, cf.: https://pulsar.apache.org/docs/en/functions-debug/#use-log-topic
      context.getLogger().debug("Updated max temperature to {}", currTemp);
    }

    // 2. return high temperature if above threshold
    return (currTemp > HIGH_TEMPERATURE) ? currTemp : null;
  }
}
