package org.study.customer.repository;

import org.study.domain.Customer;

import java.util.List;

public interface CustomerRepository {
    Customer findById(long id);
    List<Customer> findAll();
    List<Customer> findByName(String name);
    void save(Customer customer);
    void delete(long id);
}