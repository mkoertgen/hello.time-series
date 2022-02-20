FROM FROM azul/zulu-openjdk-alpine:17 as base

FROM gradle:7.4.0-jdk17 as build
WORKDIR /usr/src/app/
COPY . .
RUN gradle build

FROM base AS final
WORKDIR /app
COPY --from=build /usr/src/app/build/libs/*.jar app.jar
CMD ["java", "-jar", "app.jar"]
