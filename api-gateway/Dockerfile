FROM openjdk:23-jdk-slim

WORKDIR /app

COPY target/api-gateway-1.0-SNAPSHOT.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"] 