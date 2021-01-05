# Stage 1: Building war file
FROM openjdk:11 AS build

WORKDIR /usr/src/app

# Build without source code to use docker caching for dependencies
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
RUN ./gradlew build || return 0
# Build with source code
COPY . .
RUN ./gradlew build -x test

# Stage 2: Running war file
FROM openjdk:11

ARG APP_HOME=/usr/src/app

WORKDIR ${APP_HOME}

COPY --from=build ${APP_HOME}/build/libs/*.war app.war

EXPOSE 8080

CMD [ "java", "-jar", "app.war" ]
