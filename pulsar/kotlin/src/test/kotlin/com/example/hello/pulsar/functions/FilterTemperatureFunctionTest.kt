package com.example.hello.pulsar.functions

import com.example.hello.pulsar.Condition
import io.mockk.mockk
import io.mockk.verify
import org.apache.pulsar.functions.api.Context
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FilterTemperatureFunctionTest {

  @Test
  fun shouldFilterTemperature() {
    val function = FilterTemperatureFunction()

    val context = mockk<Context>(relaxed = true)
    val input = Condition(42f, 13f)
    val output = function.process(input, context)

    assertThat(output).isNotNull

    verify { context.getState("maxTemperature").asFloatBuffer()}

    input.temperature = 39f
    assertThat(function.process(input, context)).isNull()
  }
}

