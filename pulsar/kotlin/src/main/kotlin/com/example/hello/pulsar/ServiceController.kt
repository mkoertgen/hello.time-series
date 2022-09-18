package com.example.hello.pulsar

import com.example.hello.pulsar.services.PulsarRunnable
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Status
import io.micronaut.http.exceptions.HttpStatusException
import org.slf4j.LoggerFactory
import java.util.concurrent.ExecutorService

abstract class ServiceController<T : PulsarRunnable>(
  private val executor: ExecutorService,
  private val service: T
) {
    @Get("/status")
    fun status(): String {
        return if (service.isRunning) "Running" else "Not running"
    }

    @Status(HttpStatus.ACCEPTED)
    @Get("/start")
    fun start(): String {
        if (service.isRunning) throw HttpStatusException(HttpStatus.PRECONDITION_FAILED, "Already running")
        executor.submit(service)
        log.info("Started service: {}", service)
        return "Started"
    }

    @Status(HttpStatus.ACCEPTED)
    @Get("/stop")
    fun stop(): String {
        if (!service.isRunning) throw HttpStatusException(HttpStatus.PRECONDITION_REQUIRED, "Not running")
        service.stop()
        log.info("Stopped service: {}", service)
        return "Stopped"
    }

  companion object {
    private val log = LoggerFactory.getLogger(ServiceController::class.java)
  }
}

@Controller("/foo")
class FooController {
  @Get("/bar")
  fun bar(): String {
    return "bar"
  }
}
