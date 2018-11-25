package com.spring.service;

import com.spring.service.entity.Customer;
import com.spring.service.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("init-db")
@Slf4j
public class CustomerServiceDbInitApplication {

  public static void main(String[] args) {
    SpringApplication.run(CustomerServiceDbInitApplication.class, args);
  }


  @Bean
  public CommandLineRunner intCustomerServiceDatabase(CustomerRepository customerRepository) {
    return (args) -> {
      log.info("Start init database");
      Customer customer1 = new Customer("paul", "Paul Ong", "1234561",
          "male", "New York", "1234567891");
      Customer customer2 = new Customer("kevin", "Kevin Wong", "1234562",
          "male", "Los Angeles", "1234567892");
      Customer customer3 = new Customer("lisa", "Hill Lisa", "1234563",
          "female", "New York", "1234567893");
      customerRepository.save(customer1);
      customerRepository.save(customer2);
      customerRepository.save(customer3);
      log.info("Init database done.");
    };
  }
}
