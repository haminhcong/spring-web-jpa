package com.spring.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "ordered_item")
@AllArgsConstructor
@NoArgsConstructor
public class OrderedItem {

  @Id
  @Column(name = "id")
  @SequenceGenerator(name="ordered_item_seq", sequenceName = "ordered_item_seq",allocationSize=100)
  @GeneratedValue(generator="ordered_item_seq")
  private Long id;

  @Column(name = "item_id")
  private Long itemID;

  @Column(name = "quantity")
  private Long quantity;

  @Column(name = "price")
  private Double price;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;

  public OrderedItem(Long itemID, Long quantity, Double price) {
    this.itemID = itemID;
    this.quantity = quantity;
    this.price = price;
  }

}
