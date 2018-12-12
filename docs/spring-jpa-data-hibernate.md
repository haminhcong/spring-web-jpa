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
  
### Spring data query via entity manager and via JPA repository, result in entity List and result in DTO List


#### Via Repository

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

### Spring transactional concurrency problem

#### Spring data lock

Ref:

- https://vladmihalcea.com/how-does-database-pessimistic-locking-interact-with-insert-update-and-delete-sql-statements/
- http://lostincoding.blogspot.com/2015/11/differences-in-jpa-entity-locking-modes.html
- https://www.objectdb.com/java/jpa/persistence/lock
- https://mobiarch.wordpress.com/2013/08/01/doing-select-for-update-in-jpa/
- https://www.byteslounge.com/tutorials/locking-in-jpa-lockmodetype
- https://vladmihalcea.com/a-beginners-guide-to-java-persistence-locking/

```java
EntityManager em;
//Step 1: SELECT FOR UPDATE
AccountBalance ab = em.find(AccountBalance.class, accountId,
    LockModeType.PESSIMISTIC_WRITE);
//Step 2: Validation check
if (ab.getBalance() - withdrawAmount < 0.00) {
   //Error handling. Abort transaction
   //...

   throw ...; //Get out of here
} 
//Step 3: Proceed with operation
ab.setBalance(ab.getBalance() - withdrawAmount);
```

- Or use with query:


```java
TypedQuery<Cart> q = em.createQuery("select c from Cart c where ...", Cart.class);
q.setLockMode(LockModeType.PESSIMISTIC_WRITE);
```

- Or use with JPA repository:


```java
interface WidgetRepository extends Repository<Widget, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Widget findOne(Long id);
}
```

```java
/**
 * Repository for Wallet.
 */
public interface WalletRepository extends CrudRepository<Wallet, Long>, JpaSpecificationExecutor<Wallet> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select w from Wallet w where w.id = :id")
    Wallet findOneForUpdate(@Param("id") Long id);
}
```

Use lock example:

```java
try {
   entityManager.getTransaction().begin();
   r = entityManager.find(Route.class, r.getPrimaryKey(), LockModeType.PESSIMISTIC_WRITE);
   r.setRoute(txtRoute.getText());
   entityManager.getTransaction().commit();
} catch (PessimisticLockException e) {
   // log & rethrow
}
```


```java
Query query = entityManager.createQuery("from Student where id = :id");
query.setParameter("id", studentId);
query.setLockMode(LockModeType.OPTIMISTIC_INCREMENT);
query.getResultList()
```

Sping locking mechanisms may be classified in two major groups: Optimistic lock and pessimistic lock.


- Pessimistic locking results in database records being locked by some process until 
the unit of work is completed so no other process is able to interfere with the locked records 
until the work is not finished. 

- Optimistic locking does not actually lock anything but relies instead in checks 
made against the database during data persistence events or 
transaction commit in order to detect if the data has been changed in the meanwhile by some other process.

##### JPA locking

- As a database abstraction layer, JPA can benefit from the implicit locking mechanisms offered by the underlying RDBMS. 
For logical locking, JPA offers an optional automated entity version control mechanism as well.

- JPA supports explicit locking for the following operations: 
  - finding an entity
  - locking an existing persistence context entity
  - refreshing an entity
  - querying through JPQL, Criteria or native queries

##### Explicit lock types

The LockModeType contains the following optimistic and pessimistic locking modes:

- NONE: In the absence of explicit locking, the application will use implicit locking (optimistic or pessimistic)
- OPTIMISTIC: 	Always issues a version check upon transaction commit, therefore ensuring optimistic locking repeatable reads.
- READ: 	Same as OPTIMISTIC.
- OPTIMISTIC_FORCE_INCREMENT: 	Always increases the entity version (even when the entity doesn’t change) and issues a version check upon transaction commit, therefore ensuring optimistic locking repeatable reads.
- WRITE: 	Same as OPTIMISTIC_FORCE_INCREMENT.
- PESSIMISTIC_READ: 	A shared lock is acquired to prevent any other transaction from acquiring a PESSIMISTIC_WRITE lock.
- PESSIMISTIC_WRITE: 	An exclusive lock is acquired to prevent any other transaction from acquiring a PESSIMISTIC_READ or a PESSIMISTIC_WRITE lock.
- PESSIMISTIC_FORCE_INCREMENT: 	A database lock is acquired to prevent any other transaction from acquiring a PESSIMISTIC_READ or a PESSIMISTIC_WRITE lock and the entity version is incremented upon transaction commit.

##### Spring data support for database isolation level

References: https://vladmihalcea.com/a-beginners-guide-to-transaction-isolation-levels-in-enterprise-java/

In a relational database system, atomicity and durability are strict properties, 
while consistency and isolation are more or less configurable. 
We cannot even separate consistency from isolation as these two properties are always related.

The lower the isolation level, the less consistent the system will get. 
From the least to the most consistent, there are four isolation levels:

- READ UNCOMMITTED
- READ COMMITTED (protecting against dirty reads)
- REPEATABLE READ (protecting against dirty and non-repeatable reads)
- SERIALIZABLE (protecting against dirty, non-repeatable reads and phantom reads)

Although the most consistent SERIALIZABLE isolation level would be the safest choice, 
most databases default to READ COMMITTED instead. 
According to Amdahl’s law, to accommodate more concurrent transactions, we have to reduce the serial fraction of our data processing. 
The shorter the lock acquisition interval, the more requests a database can process. 


Spring

Spring @Transactional annotation is used for defining a transaction boundary. As opposed to Java EE, this annotation allows us to configure:

- isolation level
- exception types rollback policy
- propagation
- read-only
- timeout


Example:

```java
@Service
public class StoreServiceImpl implements StoreService {
 
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
 
    @PersistenceContext(unitName = "persistenceUnit")
    private EntityManager entityManager;
 
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void purchase(Long productId) {        
        Session session = (Session) entityManager.getDelegate();
        session.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                LOGGER.debug("Transaction isolation level is {}", Environment.isolationLevelToString(connection.getTransactionIsolation()));
            }
        });
    }
}
```

Note: we have some problem when use JTA instead JPA in spring transaction:

```xml
<bean id="jtaTransactionManager" factory-method="getTransactionManager"
      class="bitronix.tm.TransactionManagerServices" depends-on="btmConfig, dataSource"
      destroy-method="shutdown"/>
 
<bean id="transactionManager" class="org.springframework.transaction.jta.JtaTransactionManager">
    <property name="transactionManager" ref="jtaTransactionManager"/>
    <property name="userTransaction" ref="jtaTransactionManager"/>
</bean>
```

we will have an exception when run above code:

```log
org.springframework.transaction.InvalidIsolationLevelException: 
JtaTransactionManager does not support custom isolation levels by default - 
switch 'allowCustomIsolationLevels' to 'true'
```

at this case, we need enable custom transaction manager:


```xml
<bean id="transactionManager" class="org.springframework.transaction.jta.JtaTransactionManager">
    <property name="transactionManager" ref="jtaTransactionManager"/>
    <property name="userTransaction" ref="jtaTransactionManager"/>
    <property name="allowCustomIsolationLevels" value="true"/>
</bean>
```

Ref: https://vladmihalcea.com/a-beginners-guide-to-transaction-isolation-levels-in-enterprise-java/

### Use JPA database lock with isolation level

- Ref: https://stackoverflow.com/a/30796328

Both transaction isolation and JPA Entity locking are concurrency control mechanisms.

The [transaction isolation][1] is applied on a JDBC Connection level and the scope is the transaction life-cycle itself (you can't change the transaction isolation from your current running transactions). Modern databases allow you to use both [2PL (two-phase locking)][2] isolation levels and [MVCC](https://vladmihalcea.com/how-does-mvcc-multi-version-concurrency-control-work/) ones (SNAPSHOT_ISOLATION or PostgreSQL isolation levels). In MVCC, readers do not block writers and writers do not block readers (only writers block writers).

The [Java Persistence Locking API][3] offers both database-level and application-level concurrency control, which can be split in two categories:

1. Explicit Optimistic lock modes:

 - [OPTIMISTIC][4]
 - [OPTIMISTIC_FORCE_INCREMENT][5]
 - [PESSIMISTIC_FORCE_INCREMENT][6]

The optimistic locking uses version checks in UPDATE/DELETE statements and fail on version mismatches.

2. Explicit pessimistic lock modes:

 - [PESSIMISTIC_READ][7]
 - [PESSIMISTIC_WRITE][7]

The pessimistic lock modes use a database-specific lock syntax to acquire read (shared) or write (exclusive) locks (eg. SELECT ... FOR UPDATE). 

An [explicit lock mode][8] is suitable when you run on a lower-consistency isolation level (READ_COMMITTED) and you want to acquire locks whose scope are [upgraded from query life-time to a transaction life-time][9].

  [1]: http://vladmihalcea.com/a-beginners-guide-to-transaction-isolation-levels-in-enterprise-java/
  [2]: https://vladmihalcea.com/a-beginners-guide-to-the-phantom-read-anomaly-and-how-it-differs-between-2pl-and-mvcc/
  [3]: http://vladmihalcea.com/a-beginners-guide-to-java-persistence-locking/
  [4]: http://vladmihalcea.com/hibernate-locking-patterns-how-does-optimistic-lock-mode-work/
  [5]: http://vladmihalcea.com/hibernate-locking-patterns-how-does-optimistic_force_increment-lock-mode-work/
  [6]: http://vladmihalcea.com/hibernate-locking-patterns-how-does-pessimistic_force_increment-lock-mode-work/
  [7]: http://vladmihalcea.com/hibernate-locking-patterns-how-do-pessimistic_read-and-pessimistic_write-work/
  [8]: https://vladmihalcea.com/how-does-database-pessimistic-locking-interact-with-insert-update-and-delete-sql-statements/
  [9]: http://vladmihalcea.com/a-beginners-guide-to-acid-and-database-transactions/


### Spring Jpa Implement Opstimistic locking


### Spring read-only transaction Hibernate optimization

- Ref: 

### spring transaction propagation

Ref

- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/annotation/Propagation.html
- https://www.byteslounge.com/tutorials/spring-transaction-propagation-tutorial


Mode:

- MANDATORY Support a current transaction, throw an exception if none exists.
- NESTED Execute within a nested transaction if a current transaction exists, behave like PROPAGATION_REQUIRED else.
- NEVER Execute non-transactionally, throw an exception if a transaction exists.
- NOT_SUPPORTED Execute non-transactionally, suspend the current transaction if one exists.
- REQUIRED Support a current transaction, create a new one if none exists.
- REQUIRES_NEW Create a new transaction, and suspend the current transaction if one exists.
- SUPPORTS Support a current transaction, execute non-transactionally if none exists.

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

