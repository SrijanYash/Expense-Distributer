FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -q

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /workspace/target/*.jar ./api-service.jar

EXPOSE 8085

ENV PORT=8085
ENV EUREKA_ZIPKIN_ENABLED=false
ENV USER_SERVICE_URL=http://user-service:8081
ENV GROUP_SERVICE_URL=http://group-service:8082
ENV EXPENCE_SERVICE_URL=http://expence-service:8083
ENV USER_GROUP_SERVICE_URL=http://user-group-service:8084

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-XX:InitialRAMPercentage=50.0", "-XX:+UseSerialGC", "-XX:MaxMetaspaceSize=256m", "-jar", "./api-service.jar"]
