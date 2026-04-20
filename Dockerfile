FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copy only required service pom.xml files and source code (no eureka, no zipkin)
COPY Backend/api-service/pom.xml ./api-service/pom.xml
COPY Backend/api-service/src ./api-service/src

COPY Backend/user-service/pom.xml ./user-service/pom.xml
COPY Backend/user-service/src ./user-service/src

COPY Backend/group-service/pom.xml ./group-service/pom.xml
COPY Backend/group-service/src ./group-service/src

COPY Backend/expence-service/pom.xml ./expence-service/pom.xml
COPY Backend/expence-service/src ./expence-service/src

COPY Backend/user-group-service/pom.xml ./user-group-service/pom.xml
COPY Backend/user-group-service/src ./user-group-service/src

# Build all services
RUN cd api-service && mvn clean package -DskipTests -q && \
    cd ../user-service && mvn clean package -DskipTests -q && \
    cd ../group-service && mvn clean package -DskipTests -q && \
    cd ../expence-service && mvn clean package -DskipTests -q && \
    cd ../user-group-service && mvn clean package -DskipTests -q

FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy all built jars
COPY --from=build /workspace/api-service/target/*.jar ./api-service.jar
COPY --from=build /workspace/user-service/target/*.jar ./user-service.jar
COPY --from=build /workspace/group-service/target/*.jar ./group-service.jar
COPY --from=build /workspace/expence-service/target/*.jar ./expence-service.jar
COPY --from=build /workspace/user-group-service/target/*.jar ./user-group-service.jar

# Copy startup script
COPY start.sh /app/start.sh

# Expose only API gateway port (all internal services communicate via localhost)
EXPOSE 8085

# Install bash, netcat (for health checks), and make script executable
RUN apt-get update && apt-get install -y --no-install-recommends bash netcat-openbsd && \
    chmod +x /app/start.sh && \
    rm -rf /var/lib/apt/lists/*

# Use startup script
CMD ["/app/start.sh"]
