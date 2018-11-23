package com.spring.service.dto;

import com.spring.service.entity.OrderStatus;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSummaryDTO {

  private Long orderID;

  private Long customerID;

  private String deliveryAddress;

  private String emailAddress;

  private Date orderDate;

  private String orderStatus;

  private Double totalCost;

}
