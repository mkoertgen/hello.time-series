package com.examples.hello.pulsar;

import com.examples.hello.pulsar.services.PulsarRunnable;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Status;
import io.micronaut.http.exceptions.HttpStatusException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.concurrent.ExecutorService;

@Slf4j
public abstract class ServiceController<T extends PulsarRunnable> {
  protected abstract ExecutorService getExecutor();
  protected abstract T getService();

  @Status(HttpStatus.OK)
  @Get("/status")
  public String status()
  {
    return getService().isRunning() ? "Running": "Not running";
  }

  @Status(HttpStatus.ACCEPTED)
  @Get("/start")
  public String start() {
    val service = getService();
    if (service.isRunning()) throw new HttpStatusException(HttpStatus.PRECONDITION_FAILED, "Already running");
    getExecutor().submit(service);
    log.info("Started service: {}", service);
    return "Started";
  }

  @Status(HttpStatus.ACCEPTED)
  @Get("/stop")
  public String stop() {
    val service = getService();
    if (!service.isRunning()) throw new HttpStatusException(HttpStatus.PRECONDITION_REQUIRED, "Not running");
    service.stop();
    log.info("Stopped service: {}", service);
    return "Stopped";
  }
}
