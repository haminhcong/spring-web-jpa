# Notes about spring boot config and spring cloud config

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


### Create config properties beans and validate config when run application

- Add maven dependency for spring boot config and validate properties


```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-configuration-processor</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>org.hibernate</groupId>
      <artifactId>hibernate-validator</artifactId>
      <version>6.0.13.Final</version>
    </dependency>
```

- Create config bean


```java
package com.spring.ws.configuration;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("customer-service")
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerServiceProperties {

    private IpStackProperties ipStack;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IpStackProperties{

        @NotNull
        @NotEmpty
        private String accessKey;

        @NotNull
        @NotEmpty
        private String customerAddressTestIP;
    }
}

```

- Enable config bean when spring scan bean:


```java
package com.spring.ws.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CustomerServiceProperties.class)
public class CustomerServiceConfiguration {

}
```

Set config in config file `application.yaml`

```yaml
customer-service:
  ip-stack:
    access-key: 12312334
    customer-address-test-ip: 123123
```

Now, if we missing config, application will be failed and terminate when we run it.


```yaml
customer-service:
  ip-stack:
    access-key: 12312334
    # missing customer-address-test-ip config value
```

```log
Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
2019-01-12 15:41:53.839 ERROR 13284 --- [           main] o.s.b.d.LoggingFailureAnalysisReporter   : 

***************************
APPLICATION FAILED TO START
***************************

Description:

Binding to target org.springframework.boot.context.properties.bind.BindException: Failed to bind properties under 'customer-service' to com.spring.ws.configuration.CustomerServiceProperties failed:

    Property: customer-service.ip-stack.customerAddressTestIP
    Value: null
    Reason: must not be null

    Property: customer-service.ip-stack.customerAddressTestIP
    Value: null
    Reason: must not be empty


Action:

Update your application's configuration
```

Of course, when you have config bean, you can use it in another bean

```java
@Service
public class CustomerService {

  private CustomerRepository customerRepository;
  private OrderV1Client orderV1Client;
  private IpStackClient ipStackClient;

  private CustomerServiceProperties customerServiceProperties;

  @Autowired
  public CustomerService(
      CustomerRepository customerRepository,
      OrderV1Client orderV1Client,
      IpStackClient ipStackClient,
      CustomerServiceProperties customerServiceProperties) {
    this.customerRepository = customerRepository;
    this.orderV1Client = orderV1Client;
    this.ipStackClient = ipStackClient;
    this.customerServiceProperties = customerServiceProperties;
  }

  public CustomerLocationInfoDTO getCustomerLocationInfoFromIP(String customerIP)
      throws Exception {
    if(!isPublicCustomerIP(customerIP)){
      customerIP = customerServiceProperties.getIpStack().getCustomerAddressTestIP();
    }
    CustomerInfoBasedIpDTO customerInfoBasedIpDTO = ipStackClient.getCustomerOrders(
        customerIP, customerServiceProperties.getIpStack().getAccessKey());
    CustomerLocationDTO locationInfo = customerInfoBasedIpDTO.getLocationInfo();
    CustomerLocationInfoDTO customerLocationInfoDTO = new CustomerLocationInfoDTO();
    customerLocationInfoDTO.setCountryCode(customerInfoBasedIpDTO.getCountryCode());
    customerLocationInfoDTO.setCountryName(customerInfoBasedIpDTO.getCountryName());
    customerLocationInfoDTO.setCallingCode(locationInfo.getCallingCode());
    customerLocationInfoDTO.setLanguageCode(locationInfo.getLanguages().get(0).getLanguageCode());
    customerLocationInfoDTO.setLatitude(customerInfoBasedIpDTO.getLatitude());
    customerLocationInfoDTO.setLongitude(customerInfoBasedIpDTO.getLongitude());
    return customerLocationInfoDTO;
  }
 
}

```