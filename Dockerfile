FROM gradle:jdk17-alpine AS base
LABEL authors="okjaeook"
WORKDIR /app
COPY gradle/ gradle
COPY gradlew build.gradle settings.gradle ./
RUN ./gradlew build --stacktrace || return 0
COPY src ./src

FROM base AS development
CMD ["./gradlew", "bootRun", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8080'"]

FROM base AS build
RUN ./gradlew build

FROM gradle:jdk17-alpine AS production
EXPOSE 8080
COPY --from=build /app/build/libs/*.jar /app.jar
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=production", "-jar", "/app.jar"]
