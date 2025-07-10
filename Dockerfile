FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /build/common-events
COPY ../common-events/pom.xml .
COPY ../common-events/src ./src
RUN mvn install

WORKDIR /build/notification-service
COPY ../notification-service/pom.xml .
COPY ../notification-service/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /build/notification-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]