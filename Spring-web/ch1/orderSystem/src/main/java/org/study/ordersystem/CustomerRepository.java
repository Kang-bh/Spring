package org.study.ordersystem;
import java.util.List;

public interface CustomerRepository {
    Customer findById(long id);
    List<Customer> findAll();
    void save(Customer customer);
    void delete(long id);
    List<Customer> findByName(String name);
}
