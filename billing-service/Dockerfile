# Use lightweight Java runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /security-api-gateway

COPY target/billing-service-1.0.0.jar app.jar

CMD ["java","-jar","app.jar"]
