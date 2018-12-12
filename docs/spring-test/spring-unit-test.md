# Document about how to write unit tests in a Java Application use Spring Framework

## References

- https://docs.spring.io/spring/docs/5.1.3.RELEASE/spring-framework-reference/testing.html#testing
- https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html
- https://github.com/hamvocke/spring-testing
- https://www.martinfowler.com/articles/practical-test-pyramid.html
- https://www.petrikainulainen.net/spring-mvc-test-tutorial/


## Note

> https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html#boot-features-testing-spring-applications

One of the major advantages of dependency injection is that it should make your code easier to unit test. 
You can instantiate objects by using the new operator without even involving Spring. 
You can also use mock objects instead of real dependencies.

> https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#unit-testing

Dependency injection should make your code less dependent on the container than it would be with traditional Java EE development. 
The POJOs that make up your application should be testable in JUnit or TestNG tests, with objects instantiated by using the new operator, 
**without Spring or any other container**. 
You can use mock objects (in conjunction with other valuable testing techniques) to test your code in isolation. 
If you follow the architecture recommendations for Spring, the resulting clean layering and componentization of your codebase facilitate easier unit testing. 
For example, you can test service layer objects by stubbing or mocking DAO or repository interfaces, 
without needing to access persistent data while running unit tests.

> https://stackoverflow.com/questions/44787722/spring-boot-field-injection-with-autowire-not-working-in-junit-test


`@SpringBootTest` is fairly heavyweight, and for all intents and purpose will load your entire application, 
https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html#boot-features-testing-spring-boot-applications, 
it's fairly heavyweight and dramatically affects test time. Depending on what you are trying to test you may want to look into 

 - Slice tests e.g. `@JsonTest`, `@DataJpaTest`, `@WebMvcTest` etc. , 
 https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html#boot-features-testing-spring-boot-applications-testing-autoconfigured-tests.
 Benefit of these tests are not only will they not load everything, thus faster, but will try to hunt out the relevant configurations.
 - Plain old `@ContextConfiguration`and point to the relevant `@Configuration`'s required to load beans needed for the test
  https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#contextconfiguration


> https://spring.io/blog/2016/04/15/testing-improvements-in-spring-boot-1-4

The easiest way to unit test any Spring @Component is to **not involve Spring** at all! 
It’s always best to try and structure your code so that classes can be instantiated and tested directly. Usually that boils down to a few things:

- Structure your code with clean separation of concerns so that individual parts can be unit tested. TDD is a good way to achieve this.
- Use constructor injection to ensure that objects can be instantiated directly. Don’t use field injection as it just makes your tests harder to write.


## The basic

- `@Runwith` annotation:  

 > A JUnit Runner is class that extends JUnit's abstract Runner class. 
 Runners are used for running test classes. 
 The Runner that should be used to run a test can be set using the @RunWith annotation.
 When a class is annotated with @RunWith or extends a class annotated with @RunWith, 
 JUnit will invoke the class it references to run the tests in that class instead of the runner built into JUnit. 
 

- `@RunWith(SpringJUnit4ClassRunner.class)` annotation used together with `@ContextConfiguration` Annotation :

> By @RunWith(SpringJUnit4ClassRunner.class) you tell JUnit to use an other Runner. 
In this case the SpringJUnit4ClassRunner Runner. 
The Spring Runner then handles the @ContextConfiguration Annotation.

- @RunWith(SpringJUnit4ClassRunner.class): Indicates that the class should use Spring's JUnit facilities. Tells JUnit to run using Spring’s testing support
- @ContextConfiguration(locations = {...}): Indicates which XML files contain the ApplicationContext.

### Spring support for unit test

- https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#unit-testing-support-classes
- https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html


### Write Unit Test without Spring Test Annotation `SpringJUnit4ClassRunner`

- Ref: https://spring.io/blog/2016/04/15/testing-improvements-in-spring-boot-1-4

In a project use Spring Framework, we can write Unit Test without use any support from Spring framework.
Example, we have class `MyComponent` below need to test:

```java
@Component
public class MyComponent {
    
    private final SomeService service;

    public MyComponent(SomeService service) {
        this.service = service;
    }

} 
```

When we has injection by constructor, we can create `SomeService` mocked object, then init tested class 
`MyComponent` with input is mocked object we created in previous step:

```java
@Test
public void testSomeMethod() {
    SomeService service = mock(SomeService.class);
    MyComponent component = new MyComponent(service);
    // setup mock and class component methods
}

```

### Write Unit Test with  Spring Test Annotation `SpringJUnit4ClassRunner` and mockito

#### Method 1: Use test app context and return mocked bean: https://www.baeldung.com/injecting-mocks-in-spring

- The business logic that will be tested:

```java
@Service
public class NameService {
    public String getUserName(String id) {
        return "Real user name";
    }
}

@Service
public class UserService {
 
    private NameService nameService;
 
    @Autowired
    public UserService(NameService nameService) {
        this.nameService = nameService;
    }
 
    public String getUserName(String id) {
        return nameService.getUserName(id);
    }
}
```

```java
@Profile("test")
@Configuration
public class NameServiceTestConfiguration {
    @Bean
    @Primary
    public NameService nameService() {
        return Mockito.mock(NameService.class);
    }
}
```

```java
@SpringBootApplication
public class MocksApplication {
    public static void main(String[] args) {
        SpringApplication.run(MocksApplication.class, args);
    }
}
```

```java
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MocksApplication.class)
public class UserServiceTest {
 
    @Autowired
    private UserService userService;
 
    @Autowired
    private NameService nameService;
 
    @Test
    public void whenUserIdIsProvided_thenRetrievedNameIsCorrect() {
        Mockito.when(nameService.getUserName("SomeId")).thenReturn("Mock user name");
        String testName = userService.getUserName("SomeId");
        Assert.assertEquals("Mock user name", testName);
    }
}
```

- Another approach: Use @ContextConfiguration(classes = NameServiceTestConfiguration.class) to replace
@SpringApplicationConfiguration(classes = MocksApplication.class). If you use this approach, you don't need 
`@Profile("test")` and `@ActiveProfiles("test")` annotations


In unit test we prefer using @ContextConfiguration(classes = NameServiceTestConfiguration.class) to

`@SpringApplicationConfiguration(classes = MocksApplication.class)`


## Note

- https://docs.spring.io/autorepo/docs/spring-boot/1.4.x-SNAPSHOT/api/org/springframework/boot/test/SpringApplicationConfiguration.html

```text

org.springframework.boot.test
Annotation Type SpringApplicationConfiguration

    Deprecated. 
    as of 1.4 in favor of SpringBootTest or direct use of SpringBootContextLoader.

    @ContextConfiguration(loader=SpringApplicationContextLoader.class)
     @Documented
     @Inherited
     @Retention(value=RUNTIME)
     @Target(value=TYPE)
     @Deprecated
    public @interface SpringApplicationConfiguration
    Class-level annotation that is used to determine how to load and configure an ApplicationContext for integration tests.
    
    Similar to the standard @ContextConfiguration but uses Spring Boot's SpringApplicationContextLoader.
```