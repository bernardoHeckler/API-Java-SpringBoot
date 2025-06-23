# Use a imagem base do Maven para construir a aplicação
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo pom.xml para o diretório de trabalho
# Agora ele busca o pom.xml diretamente na raiz do repositório clonado
COPY pom.xml .

# Baixa as dependências para que não precisem ser baixadas novamente se apenas o código mudar
RUN mvn dependency:go-offline

# Copia o código fonte da aplicação para o diretório de trabalho
# Agora ele busca a pasta src diretamente na raiz do repositório clonado
COPY src ./src

# Empacota a aplicação em um JAR executável
RUN mvn clean install -DskipTests

# Use a imagem base do Temurin JRE para rodar a aplicação (menor que a JDK completa)
FROM eclipse-temurin:21.0.6_7-jre-ubi9-minimal AS stage-1

# Define o diretório de trabalho
WORKDIR /app

# Copia o JAR compilado da etapa de build
# Certifique-se de que 'api-sample-0.0.1-SNAPSHOT.jar' corresponde ao nome real do seu JAR
# O nome do JAR é geralmente <artifactId>-<version>.jar, que no seu caso é api-sample-0.0.1-SNAPSHOT.jar
COPY --from=build /app/target/api-sample-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta que a aplicação vai rodar
EXPOSE 8080

# Comando para rodar a aplicação quando o container iniciar
ENTRYPOINT ["java", "-jar", "app.jar"]