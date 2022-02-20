package com.examples.hello.pulsar.producers;

import com.examples.hello.pulsar.PulsarExecutors;
import com.examples.hello.pulsar.ServiceController;
import io.micronaut.http.annotation.Controller;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;

import java.util.concurrent.ExecutorService;

@Controller("/producer")
public class ProducerController extends ServiceController<ProducerRunnable> {
  @Named(PulsarExecutors.PRODUCER)
  @Getter @Inject
  ExecutorService executor;
  @Getter @Inject
  ProducerRunnable service;
}
