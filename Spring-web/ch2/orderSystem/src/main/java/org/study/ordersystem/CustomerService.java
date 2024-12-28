package org.study.ordersystem;

import java.util.List;

public interface CustomerService {
    Customer getCustomer(long id);
    List<Customer> getCustomers();
    List<Customer> getCustomersByName(String name);
    void saveCustomer(Customer customer);
    void deleteCustomer(long id);
}
