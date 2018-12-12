package com.spring.ws;

import com.spring.ws.entity.Order;
import com.spring.ws.entity.OrderStatus;
import com.spring.ws.entity.OrderedItem;
import com.spring.ws.repository.OrderRepository;
import com.spring.ws.repository.OrderStatusRepository;
import com.spring.ws.repository.OrderedItemRepository;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("init-db")
@Slf4j
public class OrderServiceDbInitApplication {

  public static void main(String[] args) {
    SpringApplication.run(OrderServiceDbInitApplication.class, args);
  }


  @Bean
  public CommandLineRunner intCustomerServiceDatabase(
      OrderStatusRepository orderStatusRepository,
      OrderedItemRepository orderedItemRepository,
      OrderRepository orderRepository) {
    return (args) -> {
      log.info("Start init database");

      List<OrderStatus> orderStatusList =  orderStatusRepository.findAll();
      log.info("orderStatusList size: " + orderStatusList.size());
      OrderStatus storeProcessingStatus = new OrderStatus(
          "STORE-PROCESSING",
          "This item is being processed at store");
      OrderStatus deliveringStatus = new OrderStatus(
          "DELIVERING",
          "These items in order are being shipped to customer");
      OrderStatus finishAndCompleteTransaction = new OrderStatus(
          "FINISH-TRANSACTION-COMPLETED",
          "This order was shipped to customer and the transaction is completed");
      OrderStatus finishAndCustomerReject = new OrderStatus(
          "FINISH-CUSTOMER-REJECT",
          "This item was shipped to customer and customer rejected paying for this order");
      OrderStatus customerCancel = new OrderStatus(
          "CANCELED-BY-CUSTOMER",
          "This order was canceled by customer");
      OrderStatus storeCanceled = new OrderStatus(
          "CANCELED-BY-STORE-ERROR",
          "This order was canceled by store due to an error when processing");

      orderStatusRepository.save(storeProcessingStatus);
      orderStatusRepository.save(deliveringStatus);
      orderStatusRepository.save(finishAndCompleteTransaction);
      orderStatusRepository.save(finishAndCustomerReject);
      orderStatusRepository.save(customerCancel);
      orderStatusRepository.save(storeCanceled);

      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");

      OrderedItem orderedItem1 = new OrderedItem(1L, 2L, 3.0);
      OrderedItem orderedItem2 = new OrderedItem(2L, 1L, 2.0);
      Order customer1Order1 = new Order(
          1L, "New York", "paul@gmail.com",
          dateFormat.parse("2018-08-05T12:35:53 +0700"), storeProcessingStatus);

      orderedItem1.setOrder(customer1Order1);
      orderedItem2.setOrder(customer1Order1);
      ArrayList<OrderedItem> customer1Order1Items = new ArrayList<>();
      customer1Order1Items.add(orderedItem1);
      customer1Order1Items.add(orderedItem2);
      customer1Order1.setOrderedItemList(customer1Order1Items);
      customer1Order1.setTotalCost(8.0);

      OrderedItem orderedItem3 = new OrderedItem(2L, 3L, 2.0);
      Order customer1Order2 = new Order(
          1L, "Washington DC", "paul@gmail.com",
          dateFormat.parse("2018-06-25T12:35:53 +0700"), deliveringStatus);
      ArrayList<OrderedItem> customer1Order2Items = new ArrayList<>();

      orderedItem3.setOrder(customer1Order2);
      customer1Order2Items.add(orderedItem3);
      customer1Order2.setOrderedItemList(customer1Order2Items);
      customer1Order2.setTotalCost(6.0);

      OrderedItem orderedItem4 = new OrderedItem(3L, 1L, 8.5);
      Order customer2Order1 = new Order(
          2L, "Los Angeles", "kevin@gmail.com",
          dateFormat.parse("2018-10-14T12:35:53 +0700"), finishAndCompleteTransaction);
      ArrayList<OrderedItem> customer2Order1Items = new ArrayList<>();

      orderedItem4.setOrder(customer2Order1);
      customer2Order1Items.add(orderedItem4);
      customer2Order1.setOrderedItemList(customer2Order1Items);
      customer2Order1.setTotalCost(8.5);

      orderRepository.save(customer1Order1);
      orderRepository.save(customer1Order2);
      orderRepository.save(customer2Order1);

      log.info("Init database done.");
    };
  }
}
