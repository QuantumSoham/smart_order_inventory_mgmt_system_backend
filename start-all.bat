@echo off
echo =========================================
echo   Starting CHUBB Microservices
echo =========================================

echo.
echo Starting Eureka Server...
start cmd /k java -jar eureka-server\target\eureka-server-0.0.1-SNAPSHOT.jar

echo.
echo Waiting 15 seconds for Eureka to initialize...
timeout /t 15 /nobreak > nul

echo.
echo Starting API Gateway...
start cmd /k java -jar api-gateway\target\api-gateway-0.0.1-SNAPSHOT.jar

echo.
echo Waiting 5 seconds for Gateway to initialize...
timeout /t 5 /nobreak > nul

echo.
echo Starting User Service...
start cmd /k java -jar user-service\target\user-service-0.0.1-SNAPSHOT.jar

echo.
echo Starting Inventory Service...
start cmd /k java -jar inventory-service\target\inventory-service-0.0.1-SNAPSHOT.jar

echo.
echo Starting Order Service...
start cmd /k java -jar order-service\target\order-service-0.0.1-SNAPSHOT.jar

echo.
echo Starting Billing Service...
start cmd /k java -jar billing-service\target\billing-service-0.0.1-SNAPSHOT.jar

echo.
echo Starting Kafka notification Service...
start cmd /k java -jar notification-service\target\kafka-demo-0.0.1-SNAPSHOT.jar

echo.
echo =========================================
echo   All services launched successfully
echo =========================================
