# 1단계: 빌드
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8
COPY . .
RUN chmod +x gradlew && ./gradlew :service:soccerservice:build -x test -Dfile.encoding=UTF-8

# 2단계: 실행
FROM eclipse-temurin:21-jre
WORKDIR /app
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
COPY --from=builder /app/service/soccerservice/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
