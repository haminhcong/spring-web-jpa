package com.spring.ws.DTO;

import com.spring.ws.entity.OrderStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrderStatusTest {
  @Test
  public void testOrderStatusNormalTest(){
    OrderStatus finishAndCompleteTransaction = new OrderStatus(
        "FINISH-TRANSACTION-COMPLETED",
        "This order was shipped to customer and the transaction is completed");
  }
}
