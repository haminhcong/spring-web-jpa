# Testing in web service development common problems

In web service development, we usually uses theses tests to verify service quality before merge code/deploy production:

- Unit Test
- Integration Test
- Acceptance Test
- Performance Test
- ...

In most common cases, Spring support us in `Unit Test` and `Integration Test`. 

In other test types, we will have others tool to performs.

## How to separate Unit Test and Integration Test with Maven FailSafe plugin and Maven Surefire plugin

- Ref: <https://antoniogoncalves.org/2012/12/13/lets-turn-integration-tests-with-maven-to-a-first-class-citizen/>

- Config maven follow above config:


```xml
<project>
  ...
  <properties>
    <skipTests>false</skipTests>
    <skipITs>${skipTests}</skipITs>
    <skipUTs>${skipTests}</skipUTs>
  </properties>
 
  <build>
    <plugins>
 
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-failsafe-plugin</artifactId>
        <version>2.12.4</version>
        <configuration>
          <skipTests>${skipTests}</skipTests>
          <skipITs>${skipITs}</skipITs>
        </configuration>
        <executions>
          <execution>
            <goals>
              <goal>integration-test</goal>
              <goal>verify</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>2.12.4</version>
        <configuration>
          <skipTests>${skipUTs}</skipTests>
        </configuration>
      </plugin>
 
    </plugins>
  </build>
</project>
```

- To run unit tests, execute this command: `mvn clean test -DskipITs`
- To run integration tests, execute this command: `mvn clean integration-test -DskipUTs`


## References

- https://antoniogoncalves.org/2012/12/13/lets-turn-integration-tests-with-maven-to-a-first-class-citizen/
- https://stackoverflow.com/questions/6612344/prevent-unit-tests-but-allow-integration-tests-in-maven
- https://maven.apache.org/surefire/maven-failsafe-plugin/examples/junit.html
- https://www.javaworld.com/article/2074569/core-java/unit-and-integration-tests-with-maven-and-junit-categories.html
- https://stackoverflow.com/a/2607563
