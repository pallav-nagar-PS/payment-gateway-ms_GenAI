FROM openjdk:21-jdk-slim
LABEL authors="palnagar"
WORKDIR /app
COPY target/paymentgateway-service-0.0.1-SNAPSHOT.jar /app/
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "paymentgateway-service.jar"]
