package org.study.ordersystem;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

//@Repository("customerRepository")
public class CustomerRepositoryImpl implements CustomerRepository {
    private List<Customer> customers;
    public CustomerRepositoryImpl() {
        // init setting
        customers = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Customer customer = new Customer();
            customer.setId(i);
            customer.setName("Customer #" + i);
            customer.setAddress("Address " + i);
            customer.setEmail("customer_" + i + "@gmail.com");
            customers.add(customer);
        }
    }

    @Override
    public Customer findById(long id) {
        for (Customer customer : customers) {
            if (customer.getId() == id) {
                return customer;
            }
        }

        return null;
    }

    @Override
    public List<Customer> findAll() {
        return customers;
    }

    @Override
    public List<Customer> findByName(String name) {
        List<Customer> list = new ArrayList<>();
        for (Customer customer : customers) {
            if (customer.getName().equals(name)) {
                Customer cust = new Customer();
                cust.setId(customer.getId());
                cust.setName(customer.getName());
                cust.setEmail(customer.getEmail());
                list.add(cust);
            }
        }

        return list;
    }

    @Override
    public void save(Customer customer) {
        for(int i = 0; i< customers.size(); i++) {
            if (customers.get(i).getId() == customer.getId()) { // 같은 경우 update
                customers.set(i, customer);
                return;
            }
        }

        customers.add(customer);
    }

    @Override
    public void delete(long id) {
        Customer customer = findById(id);
        if(customer != null) {
            customers.remove(customer);
        }
    }
}
