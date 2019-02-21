ARG JDK_BASE_IMAGE
FROM ${JDK_BASE_IMAGE}

WORKDIR /app
COPY target/customer-service-*.jar customer-service.jar
ENTRYPOINT ["java","-Djava.security.egd=file:///dev/urandom","-jar", "/app/customer-service.jar"]
CMD ["-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:9014","--spring.profiles.active=dev"]
