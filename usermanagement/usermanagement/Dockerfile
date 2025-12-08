# Use Eclipse Temurin (the new official OpenJDK build)
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Copy built jar file into the container
COPY target/*.jar app.jar

# Expose the port (change if your service uses a different one)
EXPOSE 8070

# Command to run the app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]