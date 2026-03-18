FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
ARG SERVICE_NAME
COPY ${SERVICE_NAME}/build/libs/*.jar app.jar

ENV JAVA_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1 -Dfile.encoding=UTF-8 -Dspring.output.ansi.enabled=always"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]