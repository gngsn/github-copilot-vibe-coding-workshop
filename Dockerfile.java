# Multi-stage Dockerfile for Java Spring Boot Application

# Stage 1: Build stage with Microsoft OpenJDK 21 JDK
FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu AS build-stage

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and build files
COPY java/socialapp/gradle/ gradle/
COPY java/socialapp/gradlew .
COPY java/socialapp/gradlew.bat .
COPY java/socialapp/build.gradle .
COPY java/socialapp/settings.gradle .

# Make gradlew executable
RUN chmod +x gradlew

# Download dependencies (this layer will be cached if dependencies don't change)
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY java/socialapp/src/ src/

# Build the application
RUN ./gradlew build --no-daemon -x test

# Extract JRE from JDK for smaller runtime image
RUN jlink \
    --add-modules ALL-MODULE-PATH \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=2 \
    --output /opt/jre

# Stage 2: Runtime stage with extracted JRE
FROM ubuntu:22.04 AS runtime-stage

# Install required packages and create app user
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        ca-certificates \
        sqlite3 && \
    rm -rf /var/lib/apt/lists/* && \
    groupadd -r appuser && \
    useradd -r -g appuser appuser

# Copy JRE from build stage
COPY --from=build-stage /opt/jre /opt/jre

# Set JAVA_HOME and update PATH
ENV JAVA_HOME=/opt/jre
ENV PATH="$JAVA_HOME/bin:$PATH"

# Set working directory
WORKDIR /app

# Copy the built JAR file from build stage
COPY --from=build-stage /app/build/libs/*.jar app.jar

# Create SQLite database file (empty database will be initialized by application)
RUN touch sns_api.db && \
    chown appuser:appuser sns_api.db && \
    chmod 664 sns_api.db

# Change ownership of the app directory to appuser
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Environment variables for Codespaces
ENV CODESPACE_NAME=${CODESPACE_NAME}
ENV GITHUB_CODESPACES_PORT_FORWARDING_DOMAIN=${GITHUB_CODESPACES_PORT_FORWARDING_DOMAIN}

# Expose port 8080
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/api/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
