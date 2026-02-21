FROM openjdk:17.0.2-jdk
# Set working directory
WORKDIR /app

# Copy backend jar into container
COPY target/backend-1.0-SNAPSHOT.jar backend.jar
# Expose port that your backend uses
EXPOSE 8080

# Run the backend jar
CMD ["java", "-jar", "backend.jar"]
