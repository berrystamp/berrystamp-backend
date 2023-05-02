FROM maven:3.8.2-jdk-11 AS build
COPY . .
RUN mvn clean package -Pprod -DskipTests

FROM openjdk:17-jdk-slim
COPY --from=build /target/berry-backend-service-0.0.1-SNAPSHOT.jar application.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]
