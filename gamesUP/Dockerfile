# Multi-stage build for Spring Boot (GamesUP)
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

# Copy Maven wrapper and pom
COPY mvnw* pom.xml ./
COPY .mvn .mvn

# Download dependencies (go-offline)
RUN chmod +x mvnw && ./mvnw -q -B dependency:go-offline

# Copy source
COPY src src

# Build application
RUN ./mvnw -q -B package -DskipTests

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
