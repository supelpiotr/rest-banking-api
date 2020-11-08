FROM openjdk:8
ADD target/rest-banking-api-0.0.1-SNAPSHOT.jar rest-banking-api-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "rest-banking-api-0.0.1-SNAPSHOT.jar"]