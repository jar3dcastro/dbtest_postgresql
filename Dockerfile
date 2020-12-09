FROM gradle:6.5.1-jdk8 AS build
COPY --chown=gradle:gradle . /home/gradle/
WORKDIR /home/gradle/
RUN gradle shadowJar

FROM openjdk:8-jre-alpine
ENV FAT_JAR_FILE dbtest-1.0.0-SNAPSHOT-deploy.jar
ENV CONFIG_FILE application.json
EXPOSE 8080
COPY --from=build /home/gradle/build/libs/$FAT_JAR_FILE /app/
COPY --from=build /home/gradle/config/$CONFIG_FILE /app/
WORKDIR /app/
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $FAT_JAR_FILE -conf $CONFIG_FILE"]
