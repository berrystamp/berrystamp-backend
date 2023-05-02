FROM adoptopenjdk/openjdk17:alpine-jre
COPY ./target/berry-backend-service-0.0.1-SNAPSHOT.jar application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]
