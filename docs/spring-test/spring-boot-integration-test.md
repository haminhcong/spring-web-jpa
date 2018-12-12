# Document about how to write integration tests , data jpa tests, http client tests for a spring boot project


## References

- https://www.ebayinc.com/stories/blogs/tech/your-own-spring-test-context/
- https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html




## @SpringBootTest Annotation



## @SpringBootTest Datasource config problem.

If you don't config datasource properties for SpringBootTest annotated class, you will get this error:


```log

***************************
APPLICATION FAILED TO START
***************************

Description:

Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.

Reason: Failed to determine a suitable driver class


Action:

Consider the following:
	If you want an embedded database (H2, HSQL or Derby), please put it on the classpath.
	If you have database settings to be loaded from a particular profile you may need to activate it (no profiles are currently active).

```

To fix this problem, you can:

- Add config.properties to test resources
- Add H2 dependencies to project classpath (dependency eg. Maven). Spring boot will auto detect H2 and setup auto generated config like this:


```config
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```

As I mentioned earlier, Spring Boot is an opinionated framework and does all the default configurations based on the dependencies available in the classpath.

Since we added H2 as a dependency, Spring Boot knows that in this project we will be connecting to the H2 database, so it auto-configures H2-related properties like the database URL, username, password, etc.

Ref: 

- http://www.springboottutorial.com/spring-boot-and-h2-in-memory-database
- https://stackabuse.com/integrating-h2-database-with-spring-boot/

Therefore, to test Spring boot test class, you should config Datasource properties in test resource properties file.


Another approach for this problem: Disable DataAutoconfiguration:

Ref: https://stackoverflow.com/questions/49475177/failed-to-auto-configure-a-datasource-spring-datasource-url-is-not-specified

```yaml
spring:
  application:
    name: customer-service
  cloud:
    config:
      enabled: false
      discovery:
        enabled: false
  autoconfigure:
    exclude: org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
```

but with this approach, we catch error when repository bean is not initialized:

```text
2018-12-12 02:11:39.539  WARN 12829 --- [           main] ConfigServletWebServerApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'customerController' defined in file [/home/cong/github-repo/spring-web-jpa/customer-service/target/classes/com/spring/ws/controller/v1/CustomerController.class]: Unsatisfied dependency expressed through constructor parameter 0; nested exception is org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'customerService' defined in file [/home/cong/github-repo/spring-web-jpa/customer-service/target/classes/com/spring/ws/service/CustomerService.class]: Unsatisfied dependency expressed through constructor parameter 0; nested exception is org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'com.spring.ws.repository.CustomerRepository' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {}
2018-12-12 02:11:39.546  INFO 12829 --- [           main] o.apache.catalina.core.StandardService   : Stopping service [Tomcat]
2018-12-12 02:11:39.561  INFO 12829 --- [           main] ConditionEvaluationReportLoggingListener : 

Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
2018-12-12 02:11:39.681 ERROR 12829 --- [           main] o.s.b.d.LoggingFailureAnalysisReporter   : 

***************************
APPLICATION FAILED TO START
***************************

Description:

Parameter 0 of constructor in com.spring.ws.service.CustomerService required a bean of type 'com.spring.ws.repository.CustomerRepository' that could not be found.


Action:

Consider defining a bean of type 'com.spring.ws.repository.CustomerRepository' in your configuration.

2018-12-12 02:11:39.687 ERROR 12829 --- [           main] o.s.test.context.TestContextManager      : Caught exception while allowing TestExecutionListener [org.springframework.boot.test.autoconfigure.SpringBootDependencyInjectionTestExecutionListener@2b76ff4e] to prepare test instance [com.spring.ws.service.CustomerServiceTest@403f0a22]

java.lang.IllegalStateException: Failed to load ApplicationContext

```

To solve this problem, we can create TestMockBean class for wire and replace for real datasource bean.

## Use H2 Database when do integration test, use H2 Console

Ref: 

- http://www.springboottutorial.com/spring-boot-and-h2-in-memory-database
- https://stackabuse.com/integrating-h2-database-with-spring-boot/



## Method 1: Use test app context and return mocked bean: https://www.baeldung.com/injecting-mocks-in-spring

- WARNING: https://stackoverflow.com/questions/39417530/what-is-the-proper-annotation-since-springapplicationconfiguration-webintegra

```java
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class MyTest {
}
```


as a replacement to, one of many:

```java
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(MyApp.class)
@WebIntegrationTest
public class MyTest {
}
```


May be this above approach is usually used in integration test ? 
## Problems

### How to disable cloud config when run test in a spring boot application ?

-> https://stackoverflow.com/questions/41985262/spring-boot-test-overriding-bootstrap-properties

```text

If you are using the `@SpringBootTest` annotation you can override properties in the `bootstrap.properties` with the following:

    @SpringBootTest(properties = "spring.cloud.config.enabled=false")

Otherwise, you can:

 1. Add `@ActiveProfiles('test')` to your test class
 2. Create a file named `bootstrap-test.properties`
 3. Add the properties you want to overwrite e.g. `spring.cloud.config.enabled=false`

**Update:** If you want to disable spring cloud config for **all** tests, you can simply create a `bootstrap.properties` inside your `test/resources` folder with the following property:

`spring.cloud.config.enabled=false`
```

### Spring boot application init data for demo and testing
  - https://stackoverflow.com/questions/38040572/spring-boot-loading-initial-data

```java
@Bean
public CommandLineRunner loadData(CustomerRepository repository) {
    return (args) -> {
        // save a couple of customers
        repository.save(new Customer("Jack", "Bauer"));
        repository.save(new Customer("Chloe", "O'Brian"));
        repository.save(new Customer("Kim", "Bauer"));
        repository.save(new Customer("David", "Palmer"));
        repository.save(new Customer("Michelle", "Dessler"));

        // fetch all customers
        log.info("Customers found with findAll():");
        log.info("-------------------------------");
        for (Customer customer : repository.findAll()) {
            log.info(customer.toString());
        }
        log.info("");

        // fetch an individual customer by ID
        Customer customer = repository.findOne(1L);
        log.info("Customer found with findOne(1L):");
        log.info("--------------------------------");
        log.info(customer.toString());
        log.info("");

        // fetch customers by last name
        log.info("Customer found with findByLastNameStartsWithIgnoreCase('Bauer'):");
        log.info("--------------------------------------------");
        for (Customer bauer : repository
                .findByLastNameStartsWithIgnoreCase("Bauer")) {
            log.info(bauer.toString());
        }
        log.info("");
    }
}
```



## JPA data tests

- https://stackoverflow.com/questions/48878747/junit-test-cases-for-jpa-repositories
- https://www.springbyexample.org/examples/contact-test-dao.html
- https://zsoltfabok.com/blog/2017/04/testing-the-data-access-layer-in-spring-boot/
- https://krishnasblog.com/2013/02/21/junit-testing-of-spring-mvc-application-testing-dao-layer/
- https://howtodoinjava.com/best-practices/how-you-should-unit-test-dao-layer/
- https://stackoverflow.com/questions/9377701/spring-jpa-testing-dao-layer-with-multiple-databases-in-a-ci-environment

## Unit tests

## Integration tests

## Books


## References