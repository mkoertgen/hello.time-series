package com.example.hello.pulsar.functions

import com.example.hello.pulsar.Condition
import org.apache.pulsar.functions.api.Context
import org.apache.pulsar.functions.api.Function
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class FilterTemperatureFunction : Function<Condition, Float> {
  private val log: Logger = LoggerFactory.getLogger(FilterTemperatureFunction::class.java)
  private val maxTemperatureKey = "maxTemperature"
  private val highTemperature = 40f

  override fun process(input: Condition, context: Context): Float? {
    val currTemp = input.temperature

    // 1. update max temperature
    val state = context.getState(maxTemperatureKey).asFloatBuffer()
    if (currTemp > state.get(0)) {
      state.put(0, currTemp)

      // publish to log topic, cf.: https://pulsar.apache.org/docs/en/functions-debug/#use-log-topic
      log.debug("Updated max temperature to {}", currTemp)
    }

    // 2. return high temperature if above threshold
    return if (currTemp > highTemperature) currTemp else null
  }
}

