# Stage 1: Build
FROM maven:3.9.8-amazoncorretto-21 AS build

WORKDIR /app

# Copy toàn bộ project (bao gồm parent POM và các module)
COPY . .

# Build toàn bộ project từ parent POM
RUN mvn clean install -DskipTests

# Chuyển sang thư mục user-service để package service cụ thể
WORKDIR /app/user-service
RUN mvn package -DskipTests

# Stage 2: Runtime
FROM amazoncorretto:21.0.4

WORKDIR /app

# Copy file JAR từ stage build
COPY --from=build /app/user-service/target/*.jar app.jar

# Khởi chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
