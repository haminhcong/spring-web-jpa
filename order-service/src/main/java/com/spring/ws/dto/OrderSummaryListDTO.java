package com.spring.ws.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSummaryListDTO {

  private List<OrderSummaryDTO> orderList;
  private int totalPage;
  private long totalElement;

}

