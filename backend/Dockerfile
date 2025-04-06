FROM openjdk:21-jdk-slim
WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080
CMD ["java", "-Xmx128m", "-jar", "app.jar"]
