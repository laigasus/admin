FROM gradle:jdk17-alpine AS build

WORKDIR /app
COPY gradle/ gradle
COPY gradlew build.gradle settings.gradle ./
COPY src ./src

RUN chmod +x gradlew
RUN ./gradlew build --stacktrace

FROM gradle:jdk17-alpine AS production

LABEL org.opencontainers.image.source="https://github.com/laigasus/admin"
LABEL org.opencontainers.image.description="admin example for SeSAC"
LABEL org.opencontainers.image.licenses="MIT"

EXPOSE 8080
COPY --from=build /app/build/libs/*.jar /app.jar
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=production", "-jar", "/app.jar"]