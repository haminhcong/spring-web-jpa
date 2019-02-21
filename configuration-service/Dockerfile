ARG JDK_BASE_IMAGE
FROM ${JDK_BASE_IMAGE}

WORKDIR /app
COPY target/configuration-service-*.jar configuration-service.jar
ENTRYPOINT ["java","-Djava.security.egd=file:///dev/urandom","-jar", "/app/configuration-service.jar"]
CMD ["-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:9014","--spring.profiles.active=dev"]
