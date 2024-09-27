
FROM gradle:7.5-jdk17-alpine AS builder

WORKDIR /app

COPY . .


RUN gradle build -x test


FROM openjdk:17-jdk-slim

WORKDIR /app


COPY --from=builder /app/build/libs/*.jar app.jar


VOLUME /tmp


ENTRYPOINT ["java", "-jar", "app.jar"]
