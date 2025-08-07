FROM openjdk:21-jdk-slim
LABEL authors="Salvador Alejandro Bravo Carrillo"

COPY target/gestion-usuarios-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]