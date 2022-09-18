package com.example.hello.pulsar.producers

import com.example.hello.pulsar.PulsarExecutors
import com.example.hello.pulsar.ServiceController
import io.micronaut.http.annotation.Controller
import jakarta.inject.Inject
import jakarta.inject.Named
import java.util.concurrent.ExecutorService

@Controller("/producer")
class ProducerController(
  @Inject @Named(PulsarExecutors.PRODUCER) val executor: ExecutorService,
  @Inject val service: BatchProducerRunnable
) : ServiceController<ProducerRunnable>(executor, service)
