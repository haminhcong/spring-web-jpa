package com.spring.ws.controller.v1;

import com.spring.ws.dto.OrderSummaryListDTO;
import com.spring.ws.dto.TotalOrdersDTO;
import com.spring.ws.service.OrderDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
@Slf4j
public class OrderController {

  private OrderDAO orderDAO;

  @Autowired
  public OrderController(
      OrderDAO orderDAO
  ) {
    this.orderDAO = orderDAO;
  }

  @GetMapping(value = "/customer-total-orders")
  public TotalOrdersDTO getCustomerTotalOrder(@RequestParam("customerID") Long customerID) {
    return orderDAO.getCustomerTotalOrder(customerID);
  }

  @GetMapping(value = "/orders")
  public OrderSummaryListDTO getAllOrders(
      @RequestParam(value = "statusCode", required = false, defaultValue = "") String status_code,
      @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
      @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
      @RequestParam(value = "sortKey", required = false, defaultValue = "id") String sortKey,
      @RequestParam(value = "sortDirection", required = false, defaultValue = "asc") String sortDirection
  ) {
    log.info("start logging");
    return orderDAO.getOrderList(status_code, pageIndex, pageSize, sortKey, sortDirection);
  }

  @GetMapping("/order-status-code-list")
  public List<String> getAllOrderStatusCodes() {
    return orderDAO.getOrderStatusCodeList();
  }

}