FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -q

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /workspace/target/*.jar ./expence-service.jar

EXPOSE 8083

ENV PORT=8083
ENV EUREKA_ZIPKIN_ENABLED=false

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-XX:InitialRAMPercentage=50.0", "-XX:+UseSerialGC", "-XX:MaxMetaspaceSize=64m", "-jar", "./expence-service.jar"]
