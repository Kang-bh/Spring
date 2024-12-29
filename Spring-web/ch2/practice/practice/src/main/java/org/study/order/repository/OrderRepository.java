package org.study.order.repository;

import org.study.domain.Customer;
import org.study.domain.Order;

import java.util.List;

public interface OrderRepository {
    Order findById(long id);
    List<Order> findAllOrders(Customer customer);
    void save(Order order);
    void delete(Order order);
}
