# Build stage
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /workspace/app

# Copy gradle/maven files for dependency resolution
COPY gradle gradle
COPY build.gradle settings.gradle gradlew ./

# Download dependencies
RUN ./gradlew dependencies

# Copy source code
COPY src src

# Build the JAR
RUN ./gradlew bootJar

# Runtime stage
FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
COPY --from=build /workspace/app/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]