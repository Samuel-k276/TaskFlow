FROM maven:3.9-eclipse-temurin-17 as build

WORKDIR /app

# Copie o arquivo POM
COPY pom.xml .

# Baixe todas as dependências (isso será armazenado em cache se o pom.xml não mudar)
RUN mvn dependency:go-offline -B

# Copie o código-fonte
COPY src ./src

# Compile e empacote o aplicativo
RUN mvn package -DskipTests

# Segunda etapa: imagem de execução
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copie o JAR compilado da etapa de build
COPY --from=build /app/target/*.jar app.jar

# Create .env file with environment variables from docker-compose.yml
RUN echo "DB_URL=jdbc:postgresql://postgres:5432/taskdb\n\
DB_USERNAME=postgres\n\
DB_PASSWORD=postgres\n\
JWT_SECRET=ultraMegaSecretForTokens\n\
JWT_EXPIRATION=86400000\n\
SERVER_PORT=8080\n\
FRONTEND_URL=http://localhost:4200" > /app/.env

# Exponha a porta que o aplicativo usa
EXPOSE 8080

# Configure variáveis de ambiente (podem ser substituídas durante a execução)
ENV DB_URL=jdbc:postgresql://postgres:5432/taskdb
ENV DB_USERNAME=postgres
ENV DB_PASSWORD=postgres
ENV JWT_SECRET=meuSegredoSuperSecretoParaGerarTokens
ENV JWT_EXPIRATION=86400000
ENV SERVER_PORT=8080
ENV FRONTEND_URL=http://localhost:4200

# Execute o aplicativo
ENTRYPOINT ["java", "-jar", "app.jar"]