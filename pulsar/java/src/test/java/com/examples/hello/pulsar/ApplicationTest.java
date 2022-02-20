package com.examples.hello.pulsar;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@MicronautTest
class ApplicationTest {
  @Inject
  EmbeddedApplication<?> application;

  @Test
  void testItWorks() {
    assertThat(application.isRunning()).isTrue();
  }
}
