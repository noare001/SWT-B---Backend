FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/*.jar app.jar
# Kopiere die H2-Datenbank-Dateien ins Image
COPY ../swtKidsAppH2Db.mv.db /app/db/swtKidsAppH2Db.mv.db
COPY ../swtKidsAppH2Db.trace.db /app/db/swtKidsAppH2Db.trace.db
ENTRYPOINT ["java", "-jar", "app.jar"]
