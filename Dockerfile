FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /authentication

COPY ../util/pom.xml /util/pom.xml
WORKDIR /util
RUN mvn dependency:go-offline -B

COPY ../util .
RUN mvn -f /util/pom.xml clean install -DskipTests

WORKDIR /authentication

COPY ./authentication/pom.xml .
COPY ./authentication/authentication-model/pom.xml ./authentication-model/pom.xml
COPY ./authentication/authentication-repository/pom.xml ./authentication-repository/pom.xml
COPY ./authentication/authentication-service/pom.xml ./authentication-service/pom.xml
COPY ./authentication/authentication-web/pom.xml ./authentication-web/pom.xml
COPY ./authentication/authentication-app/pom.xml ./authentication-app/pom.xml

RUN mvn dependency:go-offline -B

COPY ./authentication .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /auth
COPY --from=build /authentication/authentication-app/target/*.jar auth.jar
EXPOSE 3003
ENTRYPOINT ["java", "-jar", "auth.jar"]
