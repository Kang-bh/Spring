package org.study.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.study.customer.service.CustomerService;
import org.study.domain.Customer;
import org.study.domain.Order;
import org.study.domain.OrderItem;
import org.study.domain.Product;
import org.study.inventory.service.InventoryService;
import org.study.order.service.OrderService;
import org.study.product.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component("orderUI")
public class OrderUI {
    private Scanner scanner;
    private CustomerService customerService;
    private ProductService productService;
    private InventoryService inventoryService;
    private OrderService orderService;
    @Autowired
    public OrderUI(OrderService orderService, CustomerService customerService, ProductService productService, InventoryService inventoryService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.productService = productService;
        this.inventoryService = inventoryService;
        scanner = new Scanner(System.in);
    }
    public void purchaseOrder() {
        showCustomers();
        System.out.print("고객 ID: ");
        long custId = scanner.nextLong();
        Customer customer = customerService.getCustomer(custId);
        System.out.println(customer);
        OrderItem item = null;
        List<OrderItem> items = new ArrayList<OrderItem>();
        while((item = getOrderItem()) != null)
            items.add(item);
        if(items.size() == 0)
            return;
        Order order = new Order(customer, items);
        orderService.purchaseOrder(order);
        System.out.println(order);
        System.out.println("주문이 완료되었습니다.");
    }

    public void getOrder() {
        showCustomers();
        System.out.print("고객 ID: ");
        long custId = scanner.nextLong();
        Customer customer = customerService.getCustomer(custId);
        System.out.println(customer);
        List<Order> orders = orderService.getOrders(customer);
        if(orders.size() == 0) {
            System.out.println("주문 내역이 없습니다.");
            return;
        }
        for(Order order : orders) {
            System.out.println(order);
        }
        System.out.print("주문 ID: ");
        long orderId = scanner.nextLong();
        Order order = orderService.getOrder(orderId);
       	if(order == null) {
            System.out.println("주문 내역이 없습니다.");
            return;
        }
        for(OrderItem item : order.getItems())
            System.out.println(item);
    }

    public void getOrders() {
        showCustomers();
        System.out.print("고객 ID: ");
        long custId = scanner.nextLong();
        Customer customer = customerService.getCustomer(custId);
        System.out.println(customer);
        List<Order> orders = orderService.getOrders(customer);
        if(orders.size() == 0) {
            System.out.println("주문 내역이 없습니다.");
            return;
        }
        for(Order order : orders) {
            System.out.println(order);
            for(OrderItem item : order.getItems()) {
                System.out.println(item);
            }
        }
    }

    public void cancelOrder() {
        showCustomers();
        System.out.print("고객 ID: ");
        long custId = scanner.nextLong();
        Customer customer = customerService.getCustomer(custId);
        System.out.println(customer);
        List<Order> orders = orderService.getOrders(customer);
        for(Order order : orders) {
            System.out.println(order);
        }
        System.out.print("주문 ID: ");
        long orderId = scanner.nextLong();
        Order order = orderService.getOrder(orderId);
        orderService.cancelOrder(order);
        System.out.println(order);
        System.out.println("주문이 취소되었습니다.");
    }

    private OrderItem getOrderItem() {
        System.out.println("제품 목록: ");
        List<Product> products = productService.getProducts();
        for(Product product : products) {
            System.out.println(product);
        }
        System.out.print("제품 ID (주문하기: 99): ");
        long prodId = scanner.nextLong();
        if (prodId == 99)
            return null;
        Product product = productService.getProduct(prodId);
        System.out.println(product);
        long inventory = inventoryService.getInventory(prodId).getQuantity();
        System.out.println("재고 수량: " + inventory);
        System.out.print("주문 수량: ");
        long quantity = scanner.nextLong();
        if(inventory < quantity) {
            System.out.println("재고가 부족합니다.");
            return null;
        }
        return new OrderItem(product, quantity);
    }

    private void showCustomers() {
        System.out.println("고객 목록: ");
        List<Customer> customers = customerService.getCustomers();
        for(Customer c : customers) {
            System.out.println(c);
        }
    }
}
