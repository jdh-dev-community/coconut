FROM eclipse-temurin:17-jdk

WORKDIR /workspace/app

# 필요한 파일과 디렉토리를 컨테이너로 복사
COPY ./gradlew /workspace/app
COPY ./settings.gradle /workspace/app
COPY ./gradle   /workspace/app/gradle/
COPY ./build.gradle /workspace/app

# 전역 프로젝트 및 quiz_service 소스 코드 복사
COPY ./global /workspace/app/global
COPY ./quiz_service /workspace/app/quiz_service

RUN ./gradlew -p ./quiz_service build -x test
RUN mv ./quiz_service/build/libs/quiz_service-0.0.1-SNAPSHOT.jar /workspace/app/app.jar

FROM eclipse-temurin:17-jre

WORKDIR /workspace/spring

COPY --from=0 /workspace/app/app.jar /workspace/spring/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]