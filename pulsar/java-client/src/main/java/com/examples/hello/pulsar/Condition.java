package com.examples.hello.pulsar;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Condition {
  // https://pulsar.apache.org/docs/en/client-libraries-java/#schema-example
  @Getter @Setter public Float temperature = 0.0f;
  @Getter @Setter public Float humidity = 0.0f;
}
