FROM eclipse-temurin:21-jdk as builder

ARG MYSQL_URL
ARG MYSQL_USERNAME
ARG MYSQL_PORT
ARG MYSQL_DATABASE
ARG MYSQL_ROOT_PASSWORD
ARG OPENAI_KEY
ARG PINECONE_KEY
ARG PINECONE_PROJECT_ID
ARG PINECONE_REGION
ARG TTS_SERVER_URL

ENV MYSQL_URL=$MYSQL_URL \
    MYSQL_USERNAME=$MYSQL_USERNAME \
    MYSQL_PORT=$MYSQL_PORT \
    MYSQL_DATABASE=$MYSQL_DATABASE \
    MYSQL_ROOT_PASSWORD=$MYSQL_ROOT_PASSWORD \
    OPENAI_KEY=$OPENAI_KEY \
    PINECONE_KEY=$PINECONE_KEY \
    PINECONE_PROJECT_ID=$PINECONE_PROJECT_ID \
    PINECONE_REGION=$PINECONE_REGION
    TTS_SERVER_URL=$TTS_SERVER_URL

WORKDIR /app
COPY . /app

# 잠시 테스트 스킵: ./gradlew clean build -x test
RUN ./gradlew clean build -x test

FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar /app/streamx-0.0.2-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "streamx-0.0.2-SNAPSHOT.jar", "--spring.profiles.active=product"]
