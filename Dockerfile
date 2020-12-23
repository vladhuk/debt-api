# Stage 1: Building war file
FROM openjdk:11 AS BUILD

ARG APP_HOME=/usr/src/app

RUN mkdir ${APP_HOME}
WORKDIR ${APP_HOME}

COPY . .
RUN ./gradlew build -x test

# Stage 2: Running war file
FROM openjdk:11

ARG APP_HOME=/usr/src/app
ARG APP_NAME=app.war

RUN mkdir ${APP_HOME}
WORKDIR ${APP_HOME}

COPY --from=BUILD ${APP_HOME}/build/libs/*.war ${APP_HOME}/${APP_NAME}

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "app.war" ]
