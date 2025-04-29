# Build stage (optional – falls du Docker-Build auch für Maven nutzen willst)
FROM maven:3.9.6-eclipse-temurin-22 AS build
WORKDIR /app
COPY . .
RUN mvn --batch-mode package

# Runtime stage
FROM eclipse-temurin:22-jre
WORKDIR /app

# Kopiere die JAR aus dem vorherigen Build
COPY --from=build /app/target/*.jar app.jar

# Anwendung starten
ENTRYPOINT ["java", "-jar", "app.jar"]

