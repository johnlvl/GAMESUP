# Multi-stage build for Spring Boot (GamesUP)
# Use official Maven image to avoid wrapper checksum issues inside containers
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copy pom for dependency resolution and cache layer
COPY pom.xml ./

# Pre-fetch dependencies to leverage Docker layer caching
RUN mvn -q -B -e -DskipTests dependency:go-offline

# Copy source code
COPY src ./src

# Build application
RUN mvn -q -B -DskipTests package

# Runtime image
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Expose port
EXPOSE 8080

# Copy jar
COPY --from=build /workspace/target/*.jar app.jar

# Environment variables (override at runtime)
ENV SPRING_PROFILES_ACTIVE=dev

ENTRYPOINT ["java","-jar","/app/app.jar"]
