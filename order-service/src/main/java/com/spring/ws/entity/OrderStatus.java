package com.spring.ws.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "order_status")
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatus {

  @Id
  @Column(name = "id")
  @SequenceGenerator(name="order_status_seq", sequenceName = "order_status_seq",allocationSize=100)
  @GeneratedValue(generator="order_status_seq")
  private Long id;

  @Column(name = "order_status_code", unique = true)
  private String orderStatusCode;

  @Column(name = "description")
  private String description;

  @OneToMany(
      cascade = CascadeType.MERGE,
      orphanRemoval = true,
      mappedBy = "orderStatus"
  )
  private List<Order> orderList = new ArrayList<>();

  public OrderStatus(String orderStatusCode, String description){
    this.orderStatusCode =  orderStatusCode;
    this.description = description;
  }
}
