package com.spring.ws.repository;

import com.spring.ws.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {

  OrderStatus findByOrderStatusCode(String orderStatusCode);

}
