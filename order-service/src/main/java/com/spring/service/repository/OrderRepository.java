package com.spring.service.repository;

import com.spring.service.entity.Order;
import com.spring.service.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  Page<Order> findAllByOrderStatus(OrderStatus orderStatus, Pageable pageable);

  @Query("SELECT COUNT(customerOrder) FROM Order customerOrder where customerOrder.customerID = :customerID")
  int countTotalCustomerOrders(@Param("customerID") Long customerID);
}