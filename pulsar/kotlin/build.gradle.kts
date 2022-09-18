plugins {
  kotlin("jvm") version "1.7.10"
  kotlin("kapt") version "1.7.10"
  kotlin("plugin.allopen") version "1.7.10"
  //--- Packaging pulsar functions https://pulsar.apache.org/docs/en/functions-package/#java
  id("com.github.johnrengelman.shadow") version "7.1.2"

  id("io.micronaut.application") version "3.6.0"
}

repositories {
  mavenCentral()
}

val pulsarVersion = "2.10.1"

dependencies {
  kapt("io.micronaut:micronaut-http-validation")
  kapt("io.micronaut.openapi:micronaut-openapi")
  //annotationProcessor("io.micronaut:micronaut-management")

  implementation("io.micronaut:micronaut-http-client")
  implementation("io.micronaut:micronaut-jackson-databind")
  implementation("io.micronaut:micronaut-management")
  implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
  implementation("io.micronaut.micrometer:micronaut-micrometer-core")
  implementation("io.micronaut.micrometer:micronaut-micrometer-registry-prometheus")
  implementation("io.micronaut.openapi:micronaut-openapi")
  implementation("io.swagger.core.v3:swagger-annotations")
  implementation("jakarta.annotation:jakarta.annotation-api")


  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib")
  runtimeOnly("ch.qos.logback:logback-classic")
  implementation("io.micronaut:micronaut-validation")

  runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

  implementation("org.apache.pulsar:pulsar-client:${pulsarVersion}")
  implementation("org.apache.pulsar:pulsar-client-admin:${pulsarVersion}")
  implementation("org.apache.pulsar:pulsar-functions-api:${pulsarVersion}")

  testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
  testImplementation("org.assertj:assertj-core:3.23.1")
  testImplementation("io.mockk:mockk:1.12.7")

  testImplementation("org.testcontainers:testcontainers:1.17.3")
  testImplementation("org.testcontainers:junit-jupiter:1.17.3")
  testImplementation("org.testcontainers:pulsar:1.17.3")
}

application {
  // Define the main class for the application.
  //mainClass.set("com.example.hello.pulsar.AppKt")
  mainClass.set("com.example.hello.pulsar.Application")
}

java {
  sourceCompatibility = JavaVersion.toVersion("17")
}


tasks {
  compileKotlin {
    kotlinOptions {
      jvmTarget = "17"
    }
  }
  compileTestKotlin {
    kotlinOptions {
      jvmTarget = "17"
    }
  }
}

graalvmNative.toolchainDetection.set(false)

micronaut {
  runtime("netty")
  testRuntime("junit5")
  processing {
    incremental(true)
    annotations("com.example.*")
  }
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
