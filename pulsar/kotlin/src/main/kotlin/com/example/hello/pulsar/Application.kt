package com.example.hello.pulsar

import io.micronaut.runtime.Micronaut
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info


@OpenAPIDefinition(
  info = Info(
    title = "hello.pulsar",
    version = "0.1.0",
    description = "Kotlin Example Project"
  )
)
//@OpenAPIManagement
//@OpenAPIInclude(classes = [ServiceController::class])
object Application {
  @JvmStatic
  fun main(args:Array<String>) {
    Micronaut.run(Application.javaClass, *args)
  }
}
