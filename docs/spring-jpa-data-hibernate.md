# Spring Data, JPA and Hibernate notes, documents and references


**The most important documents**:

-  https://docs.spring.io/spring-data/jpa/docs/current/reference/html

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

### Spring boot id generator

- To generate ID for entity, with each table has independent seq generator:
  - https://www.logicbig.com/tutorials/java-ee-tutorial/jpa/seq-generator.html
  - https://www.objectdb.com/java/jpa/entity/generated#The_Sequence_Strategy_
  - https://vladmihalcea.com/hibernate-identity-sequence-and-table-sequence-generator/
  - https://stackoverflow.com/questions/2595124/java-jpa-generators-sequencegenerator

```java

@Entity
public class MyEntity2 {
  @Id
  @SequenceGenerator(name = "mySeqGen", sequenceName = "mySeq", initialValue = 5, allocationSize = 100)
  @GeneratedValue(generator = "mySeqGen")
  @Column(name = "id")
  private int myId;
}
```
  
## Spring data query via entity manager and via JPA repository, result in entity List and result in DTO List


### Via Repository

- JPA and JPQL Query and entity result

```java
public interface UserRepository extends JpaRepository<User, Long> {

  @Query("select u from User u where u.firstname like %?1")
  List<User> findByFirstnameEndsWith(String firstname);
}
```
- JPA and SQL Native Query and entity result

```java
public interface UserRepository extends JpaRepository<User, Long> {

  @Query(value = "SELECT * FROM USERS WHERE EMAIL_ADDRESS = ?1", nativeQuery = true)
  User findByEmailAddress(String emailAddress);
}
```

- JPA and SQL Native Query with Sorting : https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.sorting

```java
public interface UserRepository extends JpaRepository<User, Long> {

  @Query("select u from User u where u.lastname like ?1%")
  List<User> findByAndSort(String lastname, Sort sort);

  @Query("select u.id, LENGTH(u.firstname) as fn_len from User u where u.lastname like ?1%")
  List<Object[]> findByAsArrayAndSort(String lastname, Sort sort);
}

repo.findByAndSort("lannister", new Sort("firstname"));               
repo.findByAndSort("stark", new Sort("LENGTH(firstname)"));           
repo.findByAndSort("targaryen", JpaSort.unsafe("LENGTH(firstname)")); 
repo.findByAsArrayAndSort("bolton", new Sort("fn_len"));              
```

- JPA and SQL, JPQL return DTO list via Projection Query:


- Method 1: https://vladmihalcea.com/the-best-way-to-map-a-projection-query-to-a-dto-with-jpa-and-hibernate/
 
![Post Entity](https://vladmihalcea.files.wordpress.com/2017/08/postentityfordtoprojection.png "Post entity")

 ```java
 
 public class PostDTO {
  
     private Long id;
  
     private String title;
  
     public PostDTO(Number id, String title) {
         this.id = id.longValue();
         this.title = title;
     }
  
     public Long getId() {
         return id;
     }
  
     public String getTitle() {
         return title;
     }
 }

List<PostDTO> postDTOs = entityManager.createQuery(
    "select new " +
    "   com.vladmihalcea.book.hpjp.hibernate.query.dto.projection.jpa.PostDTO(" +
    "       p.id, " +
    "       p.title " +
    "   ) " +
    "from Post p " +
    "where p.createdOn > :fromTimestamp", PostDTO.class)
.setParameter( "fromTimestamp", Timestamp.from(
    LocalDateTime.of( 2016, 1, 1, 0, 0, 0 )
        .toInstant( ZoneOffset.UTC ) ))
.getResultList();

List<Tuple> postDTOs = entityManager
.createNativeQuery(
    "SELECT " +
    "       p.id AS id, " +
    "       p.title AS title " +
    "FROM Post p " +
    "WHERE p.created_on > :fromTimestamp", Tuple.class)
.setParameter( "fromTimestamp", Timestamp.from(
    LocalDateTime.of( 2016, 1, 1, 0, 0, 0 )
        .toInstant( ZoneOffset.UTC ) ))
.getResultList();
 
assertFalse( postDTOs.isEmpty() );

Tuple postDTO = postDTOs.get( 0 );
assertEquals( 
    1L, 
    ((Number) postDTO.get( "id" )).longValue() 
);
 
assertEquals( 
    "High-Performance Java Persistence", 
    postDTO.get( "title" ) 
);
```

- Method 2: https://smarterco.de/spring-data-jpa-query-result-to-dto/

```java
package de.smarterco.example.dto;

public class UserNameDTO {

    private Long id;
    private String name;

    public UserNameDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
```


```java
package de.smarterco.example.repositories;

import de.smarterco.example.dto.UserNameDTO;
import de.smarterco.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User AS u")
    List<User> findAll();

    @Query("SELECT new de.smarterco.example.dto.UserNameDTO(u.id, u.name) FROM User u WHERE u.name = :name")
    List<UserNameDTO> retrieveUsernameAsDTO(@Param("name") String name);
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

