# Kotlin Example Project

## Requirements

- [Java 17](https://www.oracle.com/technetwork/java/javase/downloads/index.html)
- [Gradle](https://gradle.org/)

## Compatible IDEs

Tested on:

- IntelliJ IDEA

## Build

```shell
gradle build
# build container image
gradle dockerBuild
```

## Test

```shell
gradle test
```

## Run

```shell
gradle run # available at localhost:8081 by default
```

Next, hit

- Swagger UI: http://localhost:8081/swagger-ui.html
- Health Check: http://localhost:8081/health
- Prometheus Metrics: http://localhost:8081/prometheus/metrics

## Open Issues

### Swagger (UI) not working

Sadly, Micronaut OpenAPI does not seem to resolve nested controller methods correctly.

## Micronaut 3.6.3 Documentation

- [User Guide](https://docs.micronaut.io/3.6.3/guide/index.html)
- [API Reference](https://docs.micronaut.io/3.6.3/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/3.6.3/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)

---

- [Shadow Gradle Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)

## Feature http-client documentation

- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)
