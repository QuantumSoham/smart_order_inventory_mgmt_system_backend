# Use lightweight Java runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /notification-service

COPY target/kafka-demo-0.0.1-SNAPSHOT.jar app.jar

CMD ["java","-jar","app.jar"]
