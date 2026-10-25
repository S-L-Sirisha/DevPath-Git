# ========================================================
# DevPath Portal — Production Multi-Stage Dockerfile
# Java 21 LTS + Eclipse Temurin JRE + Non-Root Security
# ========================================================

# Stage 1: Build & Package
FROM maven:3.9.6-eclipse-temurin-21-jammy AS builder
WORKDIR /workspace

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy sources and build fat JAR
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Minimal Distroless / Hardened Runtime
FROM eclipse-temurin:21-jre-jammy AS runner

# Create non-root system user and group for security compliance
RUN groupadd -r devpath && useradd -r -g devpath -u 1001 -m devpath

WORKDIR /app

# Copy executable jar from builder stage
COPY --from=builder --chown=devpath:devpath /workspace/target/devpath-portal-1.0.0.jar app.jar

# Install curl for container health check
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*

USER devpath

# Expose server port (8085 to avoid conflicts with Oracle listener on 8080)
EXPOSE 8085

# Environment variables with sensible defaults
ENV PORT=8085 \
    SPRING_PROFILES_ACTIVE=dev \
    JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# Container Healthcheck (polled every 30s)
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8085/api/health || exit 1

# Execute application
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
