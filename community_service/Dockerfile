FROM eclipse-temurin:17-jdk

WORKDIR /workspace/app

COPY ./gradlew /workspace/app
COPY ./settings.gradle /workspace/app
COPY ./gradle   /workspace/app/gradle/
COPY ./build.gradle /workspace/app
COPY ./src /workspace/app/src/

RUN ./gradlew build -x test
RUN mv ./build/libs/community_spring-0.0.1-SNAPSHOT.jar /workspace/app/app.jar

FROM eclipse-temurin:17-jre

WORKDIR /workspace/spring

COPY --from=0 /workspace/app/app.jar /workspace/spring/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]