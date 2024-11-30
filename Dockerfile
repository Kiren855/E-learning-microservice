# Stage 1: Build
FROM maven:3.9.8-amazoncorretto-21 AS build

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests

WORKDIR /app/learning-service

RUN mvn package -DskipTests

# Stage 2: Runtime
FROM amazoncorretto:21.0.4

WORKDIR /app

COPY --from=build /app/learning-service/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
