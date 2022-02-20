package com.examples.hello.pulsar.services;

public interface PulsarRunnable extends Runnable {
  void stop();
  boolean isRunning();
}
