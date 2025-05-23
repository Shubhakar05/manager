# Use Java 21 base image
FROM openjdk:21
# Copy the renamed JAR file into the container
COPY target/manager.jar manager.jar
# Expose Spring Boot's default port
EXPOSE 8080
# Run the application
ENTRYPOINT ["java", "-jar", "manager.jar"]