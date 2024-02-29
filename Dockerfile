FROM eclipse-temurin:21.0.2_13-jdk AS builder
WORKDIR /app

COPY . .

RUN ./gradlew clean build

FROM eclipse-temurin:21.0.2_13-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*SNAPSHOT.jar /app/app.jar


ENTRYPOINT ["java", "-jar", "/app/app.jar"]
