package com.spring.service.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "customer_order")
@AllArgsConstructor
@NoArgsConstructor
public class Order {

  @Id
  @Column(name = "id")
  @SequenceGenerator(name="customer_order_seq", sequenceName = "customer_order_seq",allocationSize=100)
  @GeneratedValue(generator="customer_order_seq")
  private Long id;

  @Column(name = "customer_id")
  private Long customerID;

  @Column(name = "delivery_address")
  private String deliveryAddress;

  @Column(name = "email_address")
  private String emailAddress;

  @Column(name = "order_date")
  @Temporal(TemporalType.DATE)
  private Date orderDate;

  @Column(name = "total_cost")
  private Double totalCost;

  @ManyToOne
  @JoinColumn(name = "order_status_id")
  private OrderStatus orderStatus;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL)
  private List<OrderedItem> orderedItemList = new ArrayList<>();

  public Order(long customerID, String deliveryAddress, String emailAddress, Date orderDate,
      OrderStatus orderStatus) {
    this.customerID = customerID;
    this.deliveryAddress = deliveryAddress;
    this.emailAddress = emailAddress;
    this.orderDate = orderDate;
    this.orderStatus = orderStatus;
  }
}
