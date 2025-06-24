# ---- Stage 1: Build ----
FROM maven:3.9.10-openjdk-24 AS build

# Set working directory inside the container
WORKDIR /app

# Copy Maven project files
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
# Package the application (skip tests if needed)
RUN mvn clean package -DskipTests

# ---- Stage 2: Run ----
FROM openjdk:24-jdk-nanoserver

# Set working directory
WORKDIR /app

# Copy jar from builder stage
COPY --from=build /app/target/*.jar app.jar

# Expose application port (change if needed)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
