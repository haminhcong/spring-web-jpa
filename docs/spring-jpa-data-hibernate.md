# Spring Data, JPA and Hibernate notes, documents and references

## Problems:

### Use paging and sorting in spring data jpa: 

  - https://www.logicbig.com/tutorials/spring-framework/spring-data/pagination.html
  - https://www.logicbig.com/tutorials/spring-framework/spring-data/sorting-and-pagination.html

### Spring data web support:

- https://www.logicbig.com/tutorials/spring-framework/spring-data/web-support-with-domain-class-converter.html 
- Spring repository custom query
  - https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query
  - https://stackoverflow.com/questions/10696490/does-spring-data-jpa-have-any-way-to-count-entites-using-method-name-resolving

### Init database with spring boot commandline runner and spring data

- https://spring.io/guides/gs/accessing-data-jpa/#_create_an_application_class
- Example:

```java
package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public CommandLineRunner demo(CustomerRepository repository) {
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
			repository.findById(1L)
				.ifPresent(customer -> {
					log.info("Customer found with findById(1L):");
					log.info("--------------------------------");
					log.info(customer.toString());
					log.info("");
				});

			// fetch customers by last name
			log.info("Customer found with findByLastName('Bauer'):");
			log.info("--------------------------------------------");
			repository.findByLastName("Bauer").forEach(bauer -> {
				log.info(bauer.toString());
			});
			// for (Customer bauer : repository.findByLastName("Bauer")) {
			// 	log.info(bauer.toString());
			// }
			log.info("");
		};
	}

}
```

## Book

- Pro-JPA-2-Mastering-the-Java-Persistence-API
- Spring-Data
- Pro-JPA-2-2nd-Edition
- High-Performance-Java-Persistence

## Blog, References

- https://spring.io/guides/gs/accessing-data-jpa/
- https://thoughts-on-java.org/hibernate-tips-map-java-util-date-database-column/
- https://www.callicoder.com/hibernate-spring-boot-jpa-one-to-many-mapping-example/
- https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/
- https://stackoverflow.com/questions/47065484/one-to-many-relationship-in-spring-data-jpa
- https://www.objectdb.com/java/jpa/persistence/crud
- https://stackoverflow.com/questions/11938253/whats-the-difference-between-joincolumn-and-mappedby-when-using-a-jpa-onetoma
- https://www.youtube.com/watch?v=wn1KYxxm0wo

## Sample projects

- https://github.com/spring-projects/spring-data-examples

