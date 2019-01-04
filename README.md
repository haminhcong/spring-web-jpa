# Spring Web Jpa Project

## Introduction

- Demo project about spring-boot, spring-webmvc, spring-jpa and spring test
- Requirements:
    - Java: OpenJDK 10
    
- Project blueprint is in **Blueprint.md** file.

## Planning

## Big milestone

- Implement initial project include customer service and order service
- Apply CI/CD with Jenkins and Kubernetes or OpenStack Heat for project
- Separate each project module to one independent project. Implement item service and category service
- Implement front end for project


### Milestone 1: Implements customer service and order service

- Implement code for each service.
- Create database for each service
- Create database script for each service
- Test each service working status
- Write unit test for each service 
- Write database test for each service

## How to run this project

- Open project by Intellij IDEA IDE
- Clear old data in `cicd/stagging-env/*_database` folders
- Create database folder for each service
- Run `docker-compose up -d` to start databases for services, each service has it own database.
- Go to each service, run init program in each service to populate init database for each service with spring boot profile: `init-db`
- Run each service in project

## How to test this project 

```text

GET http://localhost:8060/api/customer-api/v1/customers

###
GET http://localhost:8060/api/customer-api/v1/customers?id=1

###
GET http://localhost:8060/api/order-api/v1/orders?customerID=2

###
GET http://localhost:8060/api/order-api/v1/order-status-code-list

###
GET http://localhost:8060/api/order-api/v1/customer-total-orders?customerID=1

###
GET http://localhost:8060/api/order-api/v1/customer-total-orders?customerID=2

```

## How to run tests

- To run unit tests, execute this command: `mvn clean test -DskipITs`
- To run integration tests, execute this command: ` mvn clean integration-test verify -DskipUT`

## Benchmark performance

```bash
docker run --rm williamyeh/wrk -t4 -c100 -d500s -H 'Host: example.com' --latency --timeout 30s http://192.168.120.1:8060/api/customer/customers\?address\=Ha%20Noi
```

## References

- https://stackoverflow.com/questions/35612778/database-schema-for-an-online-shop
- http://www.erdiagrams.com/datamodel-online-shop-idef1x.html
- https://pandaforme.gitbooks.io/introduction-to-cassandra/content/understand_the_cassandra_data_model.html