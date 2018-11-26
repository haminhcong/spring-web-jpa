package com.spring.service.repository;

import com.spring.service.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {

  OrderStatus findByOrderStatusCode(String orderStatusCode);

}
