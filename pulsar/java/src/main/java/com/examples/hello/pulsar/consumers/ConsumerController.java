package com.examples.hello.pulsar.consumers;

import com.examples.hello.pulsar.PulsarExecutors;
import com.examples.hello.pulsar.ServiceController;
import io.micronaut.http.annotation.Controller;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ExecutorService;

@Controller(value = "/consumer")
@RequiredArgsConstructor
public class ConsumerController extends ServiceController<ConsumerRunnable> {
  @Inject @Named(PulsarExecutors.CONSUMER) @Getter
  ExecutorService executor;
  @Inject @Getter
  ConsumerRunnable service;
}
