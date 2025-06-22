FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Primeiro copia apenas o POM para cache de dependências
COPY api-sample/pom.xml .
RUN mvn dependency:go-offline

# Depois copia o restante do código fonte
COPY api-sample/src ./src

# Executa o build
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21.0.6_7-jre-ubi9-minimal
WORKDIR /app

# Copia o JAR específico (assumindo que o nome termina com .jar)
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]

