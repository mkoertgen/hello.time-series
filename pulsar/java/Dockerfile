# https://medium.com/@javachampions/java-is-still-free-2-0-0-6b9aa8d6d244
FROM azul/zulu-openjdk-alpine:11-jre as base

FROM gradle:6.0.1-jdk11 as build
WORKDIR /usr/src/app/
COPY . .
RUN gradle build

FROM base AS final
WORKDIR /app
COPY --from=build /usr/src/app/build/libs/*.jar app.jar
CMD ["java", "-jar", "app.jar"]