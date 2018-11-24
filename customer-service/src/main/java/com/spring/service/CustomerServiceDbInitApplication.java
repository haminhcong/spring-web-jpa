package com.spring.service;

import com.spring.service.entity.Customer;
import com.spring.service.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class CustomerServiceDbInitApplication {

  public static void main(String[] args) {
    SpringApplication.run(CustomerServiceDbInitApplication.class, args);
  }


  @Bean
  public CommandLineRunner intCustomerServiceDatabase(CustomerRepository customerRepository) {
    return (args) -> {
      log.info("Start init database");
//      customerRepository.save(new Customer("Jack", "Bauer"));
//      customerRepository.save(new Customer("Chloe", "O'Brian"));
//      customerRepository.save(new Customer("Kim", "Bauer"));
//      customerRepository.save(new Customer("David", "Palmer"));
//      customerRepository.save(new Customer("Michelle", "Dessler"));
    };
  }
}
