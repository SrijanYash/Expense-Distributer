FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copy all service pom.xml files and source code
COPY eureka-demo/pom.xml ./eureka-demo/pom.xml
COPY eureka-demo/src ./eureka-demo/src

COPY api-service/pom.xml ./api-service/pom.xml
COPY api-service/src ./api-service/src

COPY user-service/pom.xml ./user-service/pom.xml
COPY user-service/src ./user-service/src

COPY group-service/pom.xml ./group-service/pom.xml
COPY group-service/src ./group-service/src

COPY expence-service/pom.xml ./expence-service/pom.xml
COPY expence-service/src ./expence-service/src

COPY user-group-service/pom.xml ./user-group-service/pom.xml
COPY user-group-service/src ./user-group-service/src

# Build all services
RUN cd eureka-demo && mvn clean package -DskipTests -q && \
    cd ../api-service && mvn clean package -DskipTests -q && \
    cd ../user-service && mvn clean package -DskipTests -q && \
    cd ../group-service && mvn clean package -DskipTests -q && \
    cd ../expence-service && mvn clean package -DskipTests -q && \
    cd ../user-group-service && mvn clean package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy all built jars
COPY --from=build /workspace/eureka-demo/target/*.jar ./eureka-demo.jar
COPY --from=build /workspace/api-service/target/*.jar ./api-service.jar
COPY --from=build /workspace/user-service/target/*.jar ./user-service.jar
COPY --from=build /workspace/group-service/target/*.jar ./group-service.jar
COPY --from=build /workspace/expence-service/target/*.jar ./expence-service.jar
COPY --from=build /workspace/user-group-service/target/*.jar ./user-group-service.jar

# Expose all service ports
EXPOSE 8761 8081 8082 8083 8084 8085

# Install bash for the startup script
RUN apk add --no-cache bash

# Run all services - eureka first, wait, then others, finally api gateway
CMD ["sh", "-c", "java -jar eureka-demo.jar & \
    sleep 15 && \
    java -jar user-service.jar & \
    java -jar group-service.jar & \
    java -jar expence-service.jar & \
    java -jar user-group-service.jar & \
    sleep 10 && \
    java -jar api-service.jar"]
