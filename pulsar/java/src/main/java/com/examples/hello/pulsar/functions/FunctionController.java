package com.examples.hello.pulsar.functions;

import com.examples.hello.pulsar.PulsarExecutors;
import com.examples.hello.pulsar.ServiceController;
import io.micronaut.http.annotation.Controller;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ExecutorService;

@Controller("/function")
@RequiredArgsConstructor
public class FunctionController extends ServiceController<FunctionRunnable> {
  @Inject @Named(PulsarExecutors.FUNCTIONS) @Getter
  ExecutorService executor;
  @Inject
  @Getter
  FunctionRunnable service;
}
