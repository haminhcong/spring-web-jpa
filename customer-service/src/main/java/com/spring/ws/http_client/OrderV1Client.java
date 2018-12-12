package com.spring.ws.http_client;

import com.spring.ws.dto.TotalOrdersDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("order-service")
public interface OrderV1Client {

  @RequestMapping(value = "/v1/customer-total-orders", method = RequestMethod.GET)
  TotalOrdersDTO getCustomerOrders(@RequestParam("customerID") Long customerID);
}
