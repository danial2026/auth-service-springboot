FROM adoptopenjdk/openjdk11:latest
MAINTAINER danial "danial@danials.space"

EXPOSE 9002
RUN mkdir /opt/authentication-module
WORKDIR ./authentication-module
COPY ./target/authentication-module-0.0.1-SNAPSHOT.jar /opt/authentication-module
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "/opt/authentication-module/authentication-module-0.0.1-SNAPSHOT.jar"]