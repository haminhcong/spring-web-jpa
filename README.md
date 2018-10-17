# spring-web-jpa

## Introduction

- Demo project about spring-boot, spring-webmvc, spring-jpa and spring test
- Requirements:
    - Java: Oracle JDK 10
    
## Blueprint Design

System contains three micro services: customer-service, item-service and order-service. Each service has it own separate database. The objectivities of each service:

- Customer service:
  - Show customer list
  - Show customer detail. In customer detail show order list of this customer.

- Order service:
  - Show order list.
  - Show order list by customerID.
  - Show order list by itemID.
  
- Item service:
  - Show item list.
  - Show item info. In item info show order list order this item.