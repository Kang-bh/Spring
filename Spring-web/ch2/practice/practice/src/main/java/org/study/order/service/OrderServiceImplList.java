package org.study.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.study.domain.Customer;
import org.study.domain.Order;
import org.study.domain.OrderItem;
import org.study.inventory.service.InventoryService;
import org.study.order.repository.OrderRepository;

import java.util.List;

@Service("orderService")
public class OrderServiceImplList implements OrderService{
    private OrderRepository orderRepository;
    private InventoryService inventoryService;

    @Autowired
    public OrderServiceImplList(OrderRepository orderRepository, InventoryService inventoryService) {
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
    }

    @Override
    public void purchaseOrder(Order order) {
        for(OrderItem item : order.getItems())
            this.inventoryService.takeInventory(item.getProduct().getId(), item.getQuantity());
        this.orderRepository.save(order);
    }

    @Override
    public void cancelOrder(Order order) {
        for(OrderItem item : order.getItems())
            this.inventoryService.stockInventory(item.getProduct().getId(), item.getQuantity());
        this.orderRepository.delete(order);
    }

    @Override
    public Order getOrder(long id) {
        return this.orderRepository.findById(id);
    }

    @Override
    public List<Order> getOrders(Customer customer) {
        return this.orderRepository.findAllOrders(customer);
    }

}
