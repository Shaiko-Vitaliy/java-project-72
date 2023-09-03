# FROM eclipse-temurin:20-jdk

# ARG GRADLE_VERSION=8.3

# WORKDIR /app

# COPY gradle gradle
# COPY build.gradle .
# COPY settings.gradle .
# COPY gradlew .

# RUN ./gradlew --no-daemon dependencies

# COPY src src
# COPY config config

# RUN ./gradlew --no-daemon build

# ENV JAVA_OPTS "-Xmx512M -Xms512M"
# EXPOSE 7070

# CMD java -jar build/libs/app-1.0-SNAPSHOT.jar

FROM eclipse-temurin:20-jdk

ARG GRADLE_VERSION=8.3

RUN apt-get update && apt-get install -yq unzip

RUN wget -q https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip \
    && unzip gradle-${GRADLE_VERSION}-bin.zip \
    && rm gradle-${GRADLE_VERSION}-bin.zip

ENV GRADLE_HOME=/opt/gradle

RUN mv gradle-${GRADLE_VERSION} ${GRADLE_HOME}

ENV PATH=$PATH:$GRADLE_HOME/bin

WORKDIR /app

COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY gradlew .

COPY src src
COPY config config

RUN gradle installDist

CMD ./build/install/app/bin/app
