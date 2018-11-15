package com.spring.service.service;

import com.spring.service.entity.Customer;
import com.spring.service.exception.AppException;
import com.spring.service.repository.CustomerRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.concurrent.TimeUnit;

@Service
public class CustomerService {

    //    @PersistenceContext
//    private EntityManager entityManager;
    @Autowired
    private EntityManagerFactory emf;
    private CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @HystrixCommand(fallbackMethod = "findCustomerTimeout", commandKey = "findCustomerByAddress",
            threadPoolKey = "findCustomerByAddressPool")
    public Customer findCustomerByAddress(String address) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(5000);
        return customerRepository.findByAddress(address);
    }

    public Customer findCustomerByAddressFull(String address) throws InterruptedException {
        return customerRepository.findByAddress(address);
    }

    public Customer findCustomerTimeout(String address) {
        return new Customer();
    }

    @Transactional(rollbackOn = {Exception.class, RuntimeException.class})
    public Customer updateCustomer(Long id, String address) throws AppException {
//        Session session = entityManager.unwrap(Session.class);
//        EntityManager entityManager = emf.createEntityManager();
//        EntityTransaction tx = null;
//        tx = entityManager.getTransaction();
//        tx.begin();
//        session.beginTransaction();
        Customer updateCustomer = customerRepository.findById(id).orElse(null);
        updateCustomer.setAddress(address);
        customerRepository.save(updateCustomer);
        //        entityManager.persist(updateCustomer);
        if (address.contains("HN")) {
            throw new AppException("address HN is not allowed");
            //            tx.rollback();
//            return updateCustomer;
        }
//        tx.commit();
        return updateCustomer;
    }

}
