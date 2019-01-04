package com.spring.ws.service;

import com.spring.ws.dto.OrderSummaryDTO;
import com.spring.ws.dto.OrderSummaryListDTO;
import com.spring.ws.dto.TotalOrdersDTO;
import com.spring.ws.entity.Order;
import com.spring.ws.entity.OrderStatus;
import com.spring.ws.repository.OrderRepository;
import com.spring.ws.repository.OrderStatusRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderDAO {

  private OrderRepository orderRepository;
  private OrderStatusRepository orderStatusRepository;

  @Autowired
  public OrderDAO(
      OrderRepository orderRepository,
      OrderStatusRepository orderStatusRepository
  ) {
    this.orderRepository = orderRepository;
    this.orderStatusRepository = orderStatusRepository;
  }


  public TotalOrdersDTO getCustomerTotalOrder(long customerID) {
    int totalCustomerOrders =  orderRepository.countTotalCustomerOrders(customerID);
    return  new TotalOrdersDTO(totalCustomerOrders);
  }

  public List<String> getOrderStatusCodeList() {
    List<String> orderStatusCodeList = new ArrayList<>();
    List<OrderStatus> orderStatusList = orderStatusRepository.findAll();
    for (OrderStatus orderStatus : orderStatusList) {
      orderStatusCodeList.add(orderStatus.getOrderStatusCode());
    }
    return orderStatusCodeList;
  }

  public OrderSummaryListDTO getOrderList(String statusCode, int pageIndex, int pageSize,
      String sortKey, String sortDirection) {
    Page<Order> orderListResult;
    PageRequest pageRequest = genPageRequest(pageIndex, pageSize, sortKey, sortDirection);
    if (statusCode.length() == 0) {
      orderListResult = orderRepository.findAll(pageRequest);
    } else {
      OrderStatus orderStatus = orderStatusRepository.findByOrderStatusCode(statusCode);
      orderListResult = orderRepository.findAllByOrderStatus(orderStatus, pageRequest);
    }
    return genOrderSummaryListDTO(orderListResult.getContent(),
        orderListResult.getTotalPages(), orderListResult.getTotalElements());
  }

  private PageRequest genPageRequest(int pageIndex, int pageSize,
      String sortKey, String sortDirection) {
    Sort sort;
    if (sortDirection.equals("asc")) {
      sort = Sort.by(sortKey).ascending();
    } else {
      sort = Sort.by(sortKey).descending();
    }
    return PageRequest.of(pageIndex, pageSize, sort);
  }

  private OrderSummaryListDTO genOrderSummaryListDTO(
      List<Order> orderList, int totalPage, long totalElement) {

    List<OrderSummaryDTO> orderSummaryList = new ArrayList<>();

    for (Order order : orderList) {
      OrderSummaryDTO orderSummary = new OrderSummaryDTO(
          order.getId(),
          order.getCustomerID(),
          order.getDeliveryAddress(),
          order.getEmailAddress(),
          order.getOrderDate(),
          order.getOrderStatus().getOrderStatusCode(),
          order.getTotalCost()
      );
      orderSummaryList.add(orderSummary);
    }

    return new OrderSummaryListDTO(orderSummaryList, totalPage, totalElement);
  }

}
