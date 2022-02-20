package com.examples.hello.pulsar.functions;

import com.examples.hello.pulsar.weather.conditions.Condition;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

@Slf4j
public class FilterTemperatureFunction implements Function<Condition, Float> {
  public static final String MAX_TEMPERATURE_KEY = "maxTemperature";
  public static final float HIGH_TEMPERATURE = 40f;

  @Override
  public Float process(Condition input, Context context) {
    val currTemp = input.getTemperature();

    // 1. update max temperature
    val state = context.getState(MAX_TEMPERATURE_KEY).asFloatBuffer();
    if (currTemp > state.get(0)) {
      state.put(0, currTemp);

      log.debug("Updated max temperature to {}", currTemp);
    }

    // 2. return high temperature if above threshold
    return (currTemp > HIGH_TEMPERATURE) ? currTemp : null;
  }
}
