FROM gradle:9-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle bootJar --no-daemon

FROM eclipse-temurin:17-jre
COPY --from=build /app/build/libs/*.jar /app.jar
EXPOSE 8080
HEALTHCHECK --interval=15s --timeout=4s --start-period=45s --retries=5 \
    CMD ["curl", "--fail", "--silent", "--show-error", "--max-time", "3", "http://127.0.0.1:8080/actuator/health"]
ENTRYPOINT ["java","-jar","/app.jar"]
