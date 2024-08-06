FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /opt/project

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-slim

WORKDIR /app

COPY --from=build /opt/project/target/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
