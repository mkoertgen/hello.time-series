package com.examples.hello.pulsar;

import lombok.val;
import lombok.var;
import org.apache.pulsar.functions.api.Context;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExampleFunctionTest {
  // How to debug a function:
  // 1. unit test (see here)
  // 2. local runner, cf.: https://pulsar.apache.org/docs/en/functions-debug/#debug-with-localrun-mode
  // 3. log topic, cf.: https://pulsar.apache.org/docs/en/functions-debug/#use-log-topic
  // 4. use functions cli, cf.: https://pulsar.apache.org/docs/en/functions-debug/#use-functions-cli

  @Test
  public void testUpdatesState() {
    ExampleFunction func = new ExampleFunction();

    val input = new Condition();
    val expected = ExampleFunction.HIGH_TEMPERATURE + 2f;
    input.setTemperature(expected);

    val context = mock(Context.class);
    val state = ByteBuffer.allocate(4); // 4 = 1 float
    state.asFloatBuffer().put(0, ExampleFunction.HIGH_TEMPERATURE);
    when(context.getState(ExampleFunction.MAX_TEMPERATURE_KEY)).thenReturn(state);

    var output = func.process(input, context);

    // 1a. update state
    assertThat(state.asFloatBuffer().get(0)).isEqualTo(expected);
    // 1b. update high temperature
    assertThat(output).isEqualTo(expected);

    // 2. Does not update when below max
    input.setTemperature(ExampleFunction.HIGH_TEMPERATURE);
    output = func.process(input, context);
    assertThat(state.asFloatBuffer().get(0)).isEqualTo(expected);
    assertThat(output).isNull();
  }
}
