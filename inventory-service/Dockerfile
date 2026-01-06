# Use lightweight Java runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /security-api-gateway

COPY target/inventory-service-0.0.1-SNAPSHOT.jar app.jar

CMD ["java","-jar","app.jar"]
