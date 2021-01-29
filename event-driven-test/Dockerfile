FROM openjdk:8-jdk-alpine
COPY target/event-driven-test-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8099
ENTRYPOINT ["java","-jar","/app.jar"]