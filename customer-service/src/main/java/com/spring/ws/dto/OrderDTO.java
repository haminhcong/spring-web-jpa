package com.spring.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

  private String orderCode;
  private String orderDate;
  private String emailAddress;
  private String deliveryAddress;
  private String orderStatus;
  private Long totalCost;
}
