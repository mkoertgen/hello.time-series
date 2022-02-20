package com.examples.hello.pulsar;

import io.micronaut.runtime.Micronaut;

public class Application {
  public static void main(String[] args) {
    Micronaut.build(args)
      .mainClass(Application.class)
      .defaultEnvironments("dev")
      .banner(true)
      .start();
  }
}
