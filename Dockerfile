FROM openjdk:21-slim-buster
LABEL authors="battlecr33k"

COPY ./target/schedule-0.0.1-SNAPSHOT.jar /usr/app/schedulecit.jar
WORKDIR /usr/app

ENTRYPOINT ["java", "-jar","schedulecit.jar"]