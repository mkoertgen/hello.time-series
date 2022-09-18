package com.example.hello.pulsar.consumers.batch

import com.example.hello.pulsar.PulsarExecutors
import com.example.hello.pulsar.ServiceController
import io.micronaut.http.annotation.Controller
import jakarta.inject.Inject
import jakarta.inject.Named
import java.util.concurrent.ExecutorService

@Controller(value = "/consumer/batch")
class BatchConsumerController(
  @Inject @Named(PulsarExecutors.CONSUMER) val executor: ExecutorService,
  @Inject val service: BatchConsumerRunnable
) : ServiceController<BatchConsumerRunnable>(executor, service)
