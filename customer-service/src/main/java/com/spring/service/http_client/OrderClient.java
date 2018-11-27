package com.spring.service.http_client;

import com.spring.service.dto.TotalOrdersDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("order-service")
public interface OrderClient {

  @RequestMapping(value = "/customer-total-orders", method = RequestMethod.GET)
  TotalOrdersDTO getCustomerOrders(@RequestParam("customerID") Long customerID);
}
