package com.examples.hello.pulsar.consumers.batch;

import com.examples.hello.pulsar.PulsarExecutors;
import com.examples.hello.pulsar.ServiceController;
import io.micronaut.http.annotation.Controller;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ExecutorService;

@Controller(value = "/consumer/batch")
@RequiredArgsConstructor
public class BatchConsumerController extends ServiceController<BatchConsumerRunnable> {
  @Inject
  @Named(PulsarExecutors.CONSUMER)
  @Getter
  ExecutorService executor;
  @Inject
  @Getter
  BatchConsumerRunnable service;
}
