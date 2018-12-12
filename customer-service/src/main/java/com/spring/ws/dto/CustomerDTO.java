package com.spring.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

  private String userName;
  private String gender;
  private String address;
  private String phoneNumber;
  private int totalOrders;
}
