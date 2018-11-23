package com.spring.service.service;

import com.spring.service.entity.Customer;
import com.spring.service.exception.AppException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerService {

  @PersistenceContext
  private EntityManager entityManager;
  private CustomerExtendService customerExtendService;

  @Autowired
  public CustomerService(
      CustomerExtendService customerExtendService
  ) {
    this.customerExtendService = customerExtendService;
  }

  @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
  public void addNewCustomer(Customer newCustomer) throws AppException {
    Session session = entityManager.unwrap(Session.class);
    session.save(newCustomer);
    Customer extendCustomer = new Customer();
    extendCustomer.setAddress("Extend-" + newCustomer.getAddress());
    extendCustomer.setUserName("Extend-" + newCustomer.getUserName());
    customerExtendService.addExtendNewCustomer(extendCustomer);
//    session.getTransaction().commit();
  }
}
