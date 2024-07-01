FROM openjdk:17

ARG JAR_FILE=/build/libs/Backend-1.0-SNAPSHOT.jar

COPY ${JAR_FILE} /Backend.jar

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod", "/Backend.jar"]