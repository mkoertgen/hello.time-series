import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.jetbrains.kotlin.jvm") version "1.4.31"
  application
  //--- Packaging pulsar functions https://pulsar.apache.org/docs/en/functions-package/#java
  id("com.github.johnrengelman.shadow") version "6.1.0"
}

repositories {
  mavenCentral()
}

val pulsarVersion = "2.7.1"

dependencies {
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  testImplementation("org.jetbrains.kotlin:kotlin-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
  implementation("org.apache.pulsar:pulsar-client:${pulsarVersion}")
  implementation("org.apache.pulsar:pulsar-functions-api:${pulsarVersion}")
}

application {
  // Define the main class for the application.
  mainClass.set("com.example.hello.pulsar.AppKt")
}

tasks {
  withType<KotlinCompile> { kotlinOptions { jvmTarget = "1.8" } }
}
