FROM eclipse-temurin:17-jdk

WORKDIR /workspace/app

COPY ./gradlew /workspace/app
COPY ./settings.gradle /workspace/app
COPY ./gradle   /workspace/app/gradle/
COPY ./build.gradle /workspace/app

# 전역 프로젝트 및 quiz_service 소스 코드 복사
COPY ./global /workspace/app/global
COPY ./community_service /workspace/app/community_service

RUN ./gradlew :community_service:build -x test
RUN mv ./community_service/build/libs/community_service-0.0.1-SNAPSHOT.jar /workspace/app/app.jar

FROM eclipse-temurin:17-jre

WORKDIR /workspace/spring

COPY --from=0 /workspace/app/app.jar /workspace/spring/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]