FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /workspace/app

COPY pom.xml .
COPY auth-service/pom.xml ./auth-service/pom.xml
RUN mvn -B dependency:go-offline -f auth-service/pom.xml

COPY auth-service/src ./auth-service/src
RUN mvn -B package -DskipTests -f auth-service/pom.xml

FROM eclipse-temurin:17-jre-jammy
WORKDIR /opt/app
COPY --from=build /workspace/app/auth-service/target/*.jar ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]