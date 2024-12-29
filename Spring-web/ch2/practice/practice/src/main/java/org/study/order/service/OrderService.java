package org.study.order.service;

import org.study.domain.Customer;
import org.study.domain.Order;

import java.util.List;

public interface OrderService {
    void purchaseOrder(Order order);
    void cancelOrder(Order order);
    Order getOrder(long id);
    List<Order> getOrders(Customer customer);
}
