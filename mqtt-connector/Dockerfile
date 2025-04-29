# Runtime stage
FROM eclipse-temurin:22-jre
WORKDIR /app

# Kopiere die JAR aus dem vorherigen Build
COPY --from=build /app/target/*.jar app.jar

# Anwendung starten
ENTRYPOINT ["java", "-jar", "app.jar"]

