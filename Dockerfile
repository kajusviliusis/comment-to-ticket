# Stage 1: Build React frontend
FROM node:20 AS frontend-build
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build

# Stage 2: Build Spring Boot backend
FROM maven:3.9.4-eclipse-temurin-17 AS backend-build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src

COPY --from=frontend-build /app/frontend/dist/ src/main/resources/static/
RUN mvn package -DskipTests

# Stage 3: Run the app
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=backend-build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
