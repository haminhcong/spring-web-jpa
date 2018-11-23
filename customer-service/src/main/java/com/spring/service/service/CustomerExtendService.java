package com.spring.service.service;

import com.spring.service.entity.Customer;
import com.spring.service.exception.AppException;
import com.spring.service.repository.CustomerRepository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerExtendService {

  @PersistenceContext
  private EntityManager entityManager;
  private CustomerRepository customerRepository;

  @Autowired
  public CustomerExtendService(
      CustomerRepository customerRepository
  ) {
    this.customerRepository = customerRepository;
  }

  public void addExtendNewCustomer(Customer newCustomer) throws AppException {
    Session session = entityManager.unwrap(Session.class);
    customerRepository.save(newCustomer);
//    throw new AppException("error");
  }
}
