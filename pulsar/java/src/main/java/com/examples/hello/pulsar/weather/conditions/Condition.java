package com.examples.hello.pulsar.weather.conditions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Condition {
  // https://pulsar.apache.org/docs/en/client-libraries-java/#schema-example
  @Builder.Default
  Float temperature = 0.0f;
  @Builder.Default
  Float humidity = 0.0f;
}
