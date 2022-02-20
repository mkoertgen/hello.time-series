package com.examples.hello.pulsar.services;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class PulsarRunner implements AutoCloseable, PulsarRunnable {
  @Getter
  private boolean running;

  @Override
  public void stop() {
    running = false;
  }

  @SneakyThrows
  public void run() {
    if (this.isRunning()) throw new UnsupportedOperationException("Already started");
    running = true;
    while (running) {
      try {
        callOnce();
      } catch (Exception e) {
        log.warn("Could not process", e);
      }
    }
  }

  protected abstract void callOnce() throws Exception;
}
