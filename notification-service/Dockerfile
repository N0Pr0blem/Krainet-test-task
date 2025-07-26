FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /workspace/app

COPY pom.xml .
COPY notification-service/pom.xml ./notification-service/pom.xml
RUN mvn -B dependency:go-offline -f notification-service/pom.xml

COPY notification-service/src ./notification-service/src
RUN mvn -B package -DskipTests -f notification-service/pom.xml

FROM eclipse-temurin:17-jre-jammy
WORKDIR /opt/app
COPY --from=build /workspace/app/notification-service/target/*.jar ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]