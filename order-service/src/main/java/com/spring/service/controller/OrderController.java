package com.spring.service.controller;

import com.spring.service.dto.OrderSummaryListDTO;
import com.spring.service.dto.TotalOrdersDTO;
import com.spring.service.service.OrderDAO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
      @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize,
      @RequestParam(value = "sortKey", required = false, defaultValue = "id") String sortKey,
      @RequestParam(value = "sortDirection", required = false, defaultValue = "asc") String sortDirection
  ) {
    return orderDAO.getOrderList(status_code, pageIndex, pageSize, sortKey, sortDirection);
  }

  @GetMapping("/order-status-code-list")
  public List<String> getAllOrderStatusCodes() {
    return orderDAO.getOrderStatusCodeList();
  }

}