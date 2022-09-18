package com.example.hello.pulsar

import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.Test

@MicronautTest
internal class ApplicationTest {
    @Inject
    var application: EmbeddedApplication<*>? = null
    @Test
    fun testItWorks() {
        AssertionsForClassTypes.assertThat(application!!.isRunning).isTrue
    }
}
