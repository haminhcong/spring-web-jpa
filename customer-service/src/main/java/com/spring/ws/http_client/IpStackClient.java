package com.spring.ws.http_client;

import com.spring.ws.dto.external.CustomerInfoBasedIpDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "ip-stack")
public interface IpStackClient {
  @RequestMapping(value = "/{customerIP}", method = RequestMethod.GET)
  CustomerInfoBasedIpDTO getCustomerOrders(
      @PathVariable("customerIP") String customerIP,
      @RequestParam("access_key") String accessKey) throws Exception;
}
