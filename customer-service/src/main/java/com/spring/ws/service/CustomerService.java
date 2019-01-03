package com.spring.ws.service;

import com.spring.ws.dto.CustomerDTO;
import com.spring.ws.dto.TotalOrdersDTO;
import com.spring.ws.entity.Customer;
import com.spring.ws.http_client.OrderV1Client;
import com.spring.ws.repository.CustomerRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

  private CustomerRepository customerRepository;
  private OrderV1Client orderV1Client;

  @Autowired
  public CustomerService(
      CustomerRepository customerRepository,
      OrderV1Client orderV1Client) {
    this.customerRepository = customerRepository;
    this.orderV1Client = orderV1Client;
  }

  @Transactional
  public List<Customer> getCustomerList() throws Exception{
    return customerRepository.findAll();
  }

  public CustomerDTO getCustomerDetail(Long  id) throws Exception {
    Customer customer =  getCustomer(id);
    if(customer ==null){
      return null;
    }
    TotalOrdersDTO totalCustomerOrders = orderV1Client.getCustomerOrders(id);
    return getCustomerDetail(customer, totalCustomerOrders.getTotalOrders());
  }

  private Customer getCustomer(Long id) {
    return customerRepository.findById(id).orElse(null);
  }

  private CustomerDTO getCustomerDetail(Customer customer, int totalOrders) {
    CustomerDTO customerDetail = new CustomerDTO();
    customerDetail.setUserName(customer.getUserName());
    customerDetail.setAddress(customer.getAddress());
    customerDetail.setGender(customer.getGender());
    customerDetail.setPhoneNumber(customer.getPhoneNumber());
    customerDetail.setTotalOrders(totalOrders);
    return customerDetail;
  }
}
