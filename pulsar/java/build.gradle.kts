plugins {
  id("io.freefair.lombok") version "6.4.0"
  //--- Packaging pulsar functions https://pulsar.apache.org/docs/en/functions-package/#java
  id("com.github.johnrengelman.shadow") version "7.1.2"
  id("io.micronaut.application") version "3.2.1"
}

group = "com.examples.hello.pulsar"

repositories {
  mavenCentral()
}

dependencies {
  annotationProcessor("io.micronaut:micronaut-http-validation")
  implementation("io.micronaut:micronaut-http-client")
  implementation("io.micronaut:micronaut-jackson-databind")
  implementation("io.micronaut:micronaut-management")
  implementation("io.micronaut:micronaut-runtime")
  implementation("io.micronaut.micrometer:micronaut-micrometer-core")
  implementation("io.micronaut.micrometer:micronaut-micrometer-registry-prometheus")
  implementation("jakarta.annotation:jakarta.annotation-api")
  runtimeOnly("ch.qos.logback:logback-classic")
  implementation("io.micronaut:micronaut-validation")

  testImplementation(platform("org.junit:junit-bom:5.8.2"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testImplementation("org.mockito:mockito-core:4.3.1")
  testImplementation("org.assertj:assertj-core:3.22.0")

  val pulsarVersion = "2.9.1"
  implementation("org.apache.pulsar:pulsar-client:${pulsarVersion}")
  implementation("org.apache.pulsar:pulsar-client-admin:${pulsarVersion}")
  implementation("org.apache.pulsar:pulsar-functions-api:${pulsarVersion}")

  testImplementation("org.apache.pulsar:pulsar-client:${pulsarVersion}")
  testImplementation("org.apache.pulsar:pulsar-client-admin:${pulsarVersion}")
  testImplementation("org.apache.pulsar:pulsar-functions-api:${pulsarVersion}")

  testImplementation("org.testcontainers:testcontainers:1.16.3")
  testImplementation("org.testcontainers:junit-jupiter:1.16.3")
  testImplementation("org.testcontainers:pulsar:1.16.3")
}

application {
  mainClass.set("com.examples.hello.pulsar.Application")
}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

tasks.test {
  useJUnitPlatform {
    val tags = listOf("integration")
    tags.forEach { tag ->
      if (!project.hasProperty(tag))
        excludeTags(tag)
    }
  }
  testLogging {
    events("passed", "skipped", "failed")
  }
}

graalvmNative.toolchainDetection.set(false)
micronaut {
  runtime("netty")
  testRuntime("junit5")
  processing {
    incremental(true)
    annotations("$group.*")
  }
}
