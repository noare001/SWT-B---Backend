# Kopiere die JAR-Datei in das Arbeitsverzeichnis
#COPY ./mqtt-connector/target/mqtt-connector-0.0.1-SNAPSHOT.jar /app/mqtt-connector-0.0.1-SNAPSHOT.jar

# Setze den Befehl, um die JAR-Datei auszuführen
#CMD ["java", "-jar", "mqtt-connector-0.0.1-SNAPSHOT.jar"]


# STAGE 1: Build mit Maven & Java 21
FROM maven:3.9.4-eclipse-temurin-21 AS builder

WORKDIR /build
#WORKDIR /app

# Kopiere alle Dateien ins Build-Verzeichnis
COPY . .

# Baue das Projekt
RUN mvn -pl mqtt-connector -am clean package -DskipTests

# 🏁 STAGE 2: Nur JAR + schlankes Java 21 Runtime Image
FROM openjdk:21-jdk-slim

WORKDIR /app

# Kopiere das gebaute JAR aus Stage 1
COPY --from=builder /build/mqtt-connector/target/mqtt-connector-0.0.1-SNAPSHOT.jar ./app.jar

# Port freigeben (optional)
EXPOSE 8081

# App starten
CMD ["java", "-jar", "app.jar"]
