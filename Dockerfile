#FROM eclipse-temurin:17-jdk-alpine
#COPY target/*.jar application.jar
#ENTRYPOINT ["java", "-jar", "application.jar"]

FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /
COPY /src /src
COPY pom.xml /
RUN mvn -f /pom.xml clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
WORKDIR /
COPY /src /src
COPY --from=build /target/*.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]