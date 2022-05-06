FROM openjdk:17

ENV ENVIRONMENT=prod

MAINTAINER Bennet <https://github.com/Benet5>

ADD backend/target/code-capstone-backend-0.0.1-SNAPSHOT.jar app.jar

CMD [ "sh", "-c", "java -Dspring.profiles.active=docker -jar /app.jar" ]