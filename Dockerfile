FROM maven:3.8.1-jdk-11

COPY pom.xml /pom.xml
COPY src /src

RUN mvn package

CMD ["java", "-jar", "target/game-store-1.0.0-SNAPSHOT.jar"]