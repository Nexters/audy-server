FROM openjdk:17-jdk
ADD ./build/libs/audy-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]