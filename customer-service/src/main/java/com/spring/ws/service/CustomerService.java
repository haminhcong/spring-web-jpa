package com.spring.ws.service;

import com.netflix.client.ClientException;
import com.spring.ws.configuration.CustomerServiceProperties;
import com.spring.ws.dto.external.CustomerInfoBasedIpDTO;
import com.spring.ws.dto.external.CustomerLocationDTO;
import com.spring.ws.dto.response.CustomerDTO;
import com.spring.ws.dto.external.TotalOrdersDTO;
import com.spring.ws.dto.response.CustomerLocationInfoDTO;
import com.spring.ws.entity.Customer;
import com.spring.ws.exceptions.ExternalServiceErrorException;
import com.spring.ws.http_client.IpStackClient;
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
  private IpStackClient ipStackClient;

  private CustomerServiceProperties customerServiceProperties;

  @Autowired
  public CustomerService(
      CustomerRepository customerRepository,
      OrderV1Client orderV1Client,
      IpStackClient ipStackClient,
      CustomerServiceProperties customerServiceProperties) {
    this.customerRepository = customerRepository;
    this.orderV1Client = orderV1Client;
    this.ipStackClient = ipStackClient;
    this.customerServiceProperties = customerServiceProperties;
  }

  @Transactional
  public List<Customer> getCustomerList() throws Exception {
    return customerRepository.findAll();
  }

  public CustomerDTO getCustomerDetail(Long id) throws ExternalServiceErrorException {
    Customer customer = getCustomer(id);
    if (customer == null) {
      return null;
    }
    try{
      TotalOrdersDTO totalCustomerOrders = orderV1Client.getCustomerOrders(id);
      return getCustomerDetail(customer, totalCustomerOrders.getTotalOrders());
    }catch (Exception ex){
      throw new ExternalServiceErrorException(ex.getMessage());
    }
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

  public CustomerLocationInfoDTO getCustomerLocationInfoFromIP(String customerIP)
      throws Exception {
    if(!isPublicCustomerIP(customerIP)){
      customerIP = customerServiceProperties.getIpStack().getCustomerAddressTestIP();
    }
    CustomerInfoBasedIpDTO customerInfoBasedIpDTO = ipStackClient.getCustomerOrders(
        customerIP, customerServiceProperties.getIpStack().getAccessKey());
    CustomerLocationDTO locationInfo = customerInfoBasedIpDTO.getLocationInfo();
    CustomerLocationInfoDTO customerLocationInfoDTO = new CustomerLocationInfoDTO();
    customerLocationInfoDTO.setCountryCode(customerInfoBasedIpDTO.getCountryCode());
    customerLocationInfoDTO.setCountryName(customerInfoBasedIpDTO.getCountryName());
    customerLocationInfoDTO.setCallingCode(locationInfo.getCallingCode());
    customerLocationInfoDTO.setLanguageCode(locationInfo.getLanguages().get(0).getLanguageCode());
    customerLocationInfoDTO.setLatitude(customerInfoBasedIpDTO.getLatitude());
    customerLocationInfoDTO.setLongitude(customerInfoBasedIpDTO.getLongitude());
    return customerLocationInfoDTO;
  }

  private boolean isPublicCustomerIP(String customerIP){
    if(customerIP ==null){
      return false;
    }
    if(customerIP.startsWith("192.168")
        || customerIP.startsWith("127.0.0.1")){
      return false;
    }
    return true;
  }
}
