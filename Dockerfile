FROM amazoncorretto:21.0.2-alpine3.19
COPY build/libs/AuthVerifyMicroservice-0.0.1-SNAPSHOT-plain.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
