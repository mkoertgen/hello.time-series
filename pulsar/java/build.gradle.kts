plugins {
  application
  id("io.freefair.lombok") version "5.3.3.3"
  //--- Packaginf pulsar functions https://pulsar.apache.org/docs/en/functions-package/#java
  id("com.github.johnrengelman.shadow") version "6.1.0"
  //--- 2. NAR packaging
  //id("me.ragill.nar-plugin") version "1.0.2"
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
}

repositories {
  mavenCentral()
}

val pulsarVersion = "2.7.1"

dependencies {
  compileOnly("com.google.guava:guava:30.1-jre")

  testImplementation(platform("org.junit:junit-bom:5.7.1"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testImplementation("org.mockito:mockito-core:3.9.0")
  testImplementation("org.assertj:assertj-core:3.19.0")

  implementation("org.slf4j:slf4j-api:1.7.30")
  implementation("ch.qos.logback:logback-core:1.2.3")
  implementation("ch.qos.logback:logback-classic:1.2.3")

  implementation("org.apache.pulsar:pulsar-client:${pulsarVersion}")
  implementation("org.apache.pulsar:pulsar-functions-api:${pulsarVersion}")
}

application {
  mainClass.set("com.examples.hello.pulsar.App")
}

tasks.test {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}
