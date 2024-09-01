FROM amazoncorretto:21-alpine
COPY build/libs/ds-0.0.1-SNAPSHOT.jar app.jar
ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:8090"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app.jar --debug"]