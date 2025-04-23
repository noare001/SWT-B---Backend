# Verwende ein offizielles OpenJDK-Image als Basis
FROM openjdk:21-jdk-slim

# Setze das Arbeitsverzeichnis
WORKDIR /app

# Kopiere die JAR-Datei in das Arbeitsverzeichnis
COPY ./mqtt-connector/target/mqtt-connector-0.0.1-SNAPSHOT.jar /app/mqtt-connector-0.0.1-SNAPSHOT.jar

# Setze den Befehl, um die JAR-Datei auszuführen
CMD ["java", "-jar", "mqtt-connector-0.0.1-SNAPSHOT.jar"]
