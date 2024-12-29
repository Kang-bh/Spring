package org.study.domain;

import java.time.LocalDate;
import java.util.List;

public class Order {
    private long id;
    private Customer customer;
    private LocalDate date;
    private List<OrderItem> items;

    public Order(Customer customer, List<OrderItem> items) {
        this.customer = customer;
        this.date = LocalDate.now();
        this.items = items;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public List<OrderItem> getItems() {
        return items;
    }
    public long getTotal() {
        long total = 0;
        for(OrderItem item : items) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customer=" + customer +
                ", date=" + date +
                ", items=" + items +
                '}';
    }
}
