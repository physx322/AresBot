FROM eclipse-temurin:21-jre
WORKDIR /app
COPY build/libs/ClicBot-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]