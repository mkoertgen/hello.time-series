package com.example.hello.pulsar.functions

import com.example.hello.pulsar.PulsarExecutors
import com.example.hello.pulsar.ServiceController
import io.micronaut.http.annotation.Controller
import jakarta.inject.Inject
import jakarta.inject.Named
import java.util.concurrent.ExecutorService

@Controller(value = "/function")

class FunctionController(
  @Inject @Named(PulsarExecutors.FUNCTIONS) val executor: ExecutorService,
  @Inject val service: FunctionRunnable
) : ServiceController<FunctionRunnable>(executor, service)

