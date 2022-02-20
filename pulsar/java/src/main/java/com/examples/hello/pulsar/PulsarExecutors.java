package com.examples.hello.pulsar;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Factory
public class PulsarExecutors {
  public static final String CONSUMER = "consumer";
  public static final String PRODUCER = "producer";
  public static final String FUNCTIONS = "functions";

  @Singleton
  @Named(PRODUCER)
  public ExecutorService producer() { return Executors.newFixedThreadPool(5); }

  @Singleton
  @Named(CONSUMER)
  public ExecutorService consumer() {
    return Executors.newFixedThreadPool(5);
  }

  @Singleton
  @Named(FUNCTIONS)
  public ExecutorService functions() {
    return Executors.newFixedThreadPool(5);
  }

}
