FROM maven:3.8.1-jdk-11-openj9 AS build
MAINTAINER danial "danial@danials.space"

EXPOSE 9002

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src/ ./src/

CMD ["mvn", "spring-boot:run"]