package com.spring.service.http_client;

import com.spring.service.dto.OrderDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("order-service")
public interface OrderClient {

  @RequestMapping(value = "/orders", method = RequestMethod.GET)
  List<OrderDTO> getCustomerOrders(@RequestParam("customerID") Long customerID);
}
