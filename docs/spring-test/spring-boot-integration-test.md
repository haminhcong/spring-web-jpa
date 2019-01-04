# Document about how to write integration tests , data jpa tests, http client tests for a spring boot project


## References

- https://www.ebayinc.com/stories/blogs/tech/your-own-spring-test-context/
- https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html




## @SpringBootTest Annotation



## Spring Application Context cache problem in Spring Integration Test

When using different `MockBean` in Spring Integration Tests, Spring Application Context will be reloaded.

Example: If Integration Test 1 use `MockBean a` and Integration Test 2 use `MockBean b`, 
then Application Context will be reloaded

But, If Two Integration Tests has Appplication Contexts use same MockBean set, Application Context will be cached between Two tests.

Example: Integration Test 1 use `MockBean a, MockBean b, MockBean c` and 
Integration Test 2 use `MockBean a, MockBean b, MockBean c`, then Application Context will be cached, which mean reduce tests execution time.

An solution for caching AppContext when switch Test classes is put all MockBeans needed in Some Integration Tests into a Abstract Test Class, then
each Integration Tests will extends from this Abstract Test class.

Full example of this solution can be seen in here: <https://github.com/OpenLMIS/openlmis-referencedata/commit/1664a7ba274412eec808e4378d27b7427f69eae5>


But, careful when use this solution. Mock too many bean can break tests and make each test depends on others tests. 
Ref: <https://github.com/spring-projects/spring-boot/issues/9511#issuecomment-388984678>

### References:
  
- <https://github.com/spring-projects/spring-boot/commit/0f6a13c9b3d4f3a9db50676098c535097cd24e2c>
- <https://github.com/spring-projects/spring-boot/issues/9511>
- <https://github.com/spring-projects/spring-boot/issues/7174>
- <https://github.com/spring-projects/spring-boot-issues/pull/56/files>
- <https://github.com/spring-projects/spring-boot-issues/pull/59/files>
- <https://github.com/spring-projects/spring-boot/commit/0f6a13c9b3d4f3a9db50676098c535097cd24e2c>


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

### Build GET URL for TestRestTemplate in Spring Integration Test

- Ref: <https://www.oodlestechnologies.com/blogs/Learn-To-Make-REST-calls-With-RestTemplate-In-Spring-Boot>

```java
String transactionUrl = "http://localhost:8080/api/v1/transactions";

UriComponentsBuilder builder = UriComponentsBuilder
    .fromUriString(transactionUrl)
    // Add query parameter
    .queryParam("pageNumber", "1")
    .queryParam("walletId", "2323JK")
    .queryParam("pageSize", "10");

RestTemplate restTemplate = new RestTemplate();
WalletListDTO response = restTemplate.getForObject(builder.toUriString(), walletListDTO.class);

```

## References

