package com.spring.service.repository;

import com.spring.service.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
  public OrderStatus findByOrderStatusCode(String orderStatusCode);

}
