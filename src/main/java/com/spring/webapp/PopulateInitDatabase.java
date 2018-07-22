package com.spring.webapp;

import com.spring.webapp.data_type.PhoneNumber;
import com.spring.webapp.entity.Customer;
import com.spring.webapp.entity.Note;
import com.spring.webapp.repository.CustomerRepository;
import com.spring.webapp.repository.NoteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PopulateInitDatabase {
    public static void main(String[] args) {
        SpringApplication.run(PopulateInitDatabase.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(NoteRepository noteRepository, CustomerRepository customerRepository) {
        return args -> {
            Note newNote1 = new Note();
            newNote1.setTitle("Note1");
            newNote1.setContent("Note 1 content");
            Note newNote2 = new Note();
            newNote2.setTitle("Note2");
            newNote2.setContent("Note 2 content");
            if (noteRepository.findByTitle("Note1") == null) {
                noteRepository.save(newNote1);
            }
            if (noteRepository.findByTitle("Note2") == null) {
                noteRepository.save(newNote2);
            }

            Customer customer1 = new Customer();
            customer1.setUserName("customer1");
            customer1.setAddress("Ha Noi");
            customer1.setPhoneNumber(new PhoneNumber(
                    "084", "01666313616"
            ));
            if (customerRepository.findByUserName("customer1") == null) {
                customerRepository.save(customer1);
            }
            Customer customer2 = new Customer();
            customer2.setUserName("customer2");
            customer2.setAddress("Ho Chi Minh");
            customer2.setPhoneNumber(new PhoneNumber(
                    "020", "0989414327"
            ));
            if (customerRepository.findByUserName("customer2") == null) {
                customerRepository.save(customer2);
            }

        };
    }
}
