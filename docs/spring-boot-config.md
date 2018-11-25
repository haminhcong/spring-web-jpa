# Notes about spring cloud config

## Problems:
 
### Spring boot config load orders:
  - https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html
  - https://stackoverflow.com/questions/43636201/spring-cloud-config-in-local-dev-mode
  
```text
Spring Boot uses a very particular PropertySource order that is designed to allow sensible overriding of values. Properties are considered in the following order:

    Devtools global settings properties on your home directory (~/.spring-boot-devtools.properties when devtools is active).
    @TestPropertySource annotations on your tests.
    properties attribute on your tests. Available on @SpringBootTest and the test annotations for testing a particular slice of your application.
    Command line arguments.
    Properties from SPRING_APPLICATION_JSON (inline JSON embedded in an environment variable or system property).
    ServletConfig init parameters.
    ServletContext init parameters.
    JNDI attributes from java:comp/env.
    Java System properties (System.getProperties()).
    OS environment variables.
    A RandomValuePropertySource that has properties only in random.*.
    Profile-specific application properties outside of your packaged jar (application-{profile}.properties and YAML variants).
    Profile-specific application properties packaged inside your jar (application-{profile}.properties and YAML variants).
    Application properties outside of your packaged jar (application.properties and YAML variants).
    Application properties packaged inside your jar (application.properties and YAML variants).
    @PropertySource annotations on your @Configuration classes.
    Default properties (specified by setting SpringApplication.setDefaultProperties).

```

### Disable cloud config and discovery service when test/init database:
  - https://github.com/spring-cloud/spring-cloud-commons/issues/217#issuecomment-434769757
  - https://stackoverflow.com/questions/35142105/eureka-discovery-client-selective-disable

 ```yaml
spring:
  application:
    name: app
  cloud:
    config:
      enabled: false
      discovery:
        enabled: false
eureka:
  client:
    enabled: false
```

### Disable web server when use application to test/init database

- https://stackoverflow.com/questions/29800584/how-to-prevent-spring-boot-autoconfiguration-for-spring-web

```yaml
spring:
  main:
    web-application-type: none
```

### Spring boot default config value: AutoConfig

- https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html

