# Document about how to write unit tests, data jpa tests, http tests and integration tests in a spring project

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

- Spring boot application init data for demo and testing
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

- 