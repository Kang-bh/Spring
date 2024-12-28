package org.study.ordersystem;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
//@Repository("customerRepository")
public class CustomerRepositoryImpl2 implements CustomerRepository{
    private List<Customer> customers;

    public CustomerRepositoryImpl2() {
        customers = new ArrayList<>();
        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("이일");
        customer.setAddress("서울시");
        customer.setEmail("lee1@gmail.com");
        customers.add(customer);
        Customer customer2 = new Customer();
        customer2.setId(2);
        customer2.setName("이이");
        customer2.setAddress("부산시");
        customer2.setEmail("lee2@gmail.com");
        customers.add(customer2);
        Customer customer3 = new Customer();
        customer3.setId(3);
        customer3.setName("이삼");
        customer3.setAddress("인천시");
        customer3.setEmail("lee3@gmail.com");
        customers.add(customer3);
        Customer customer4 = new Customer();
        customer4.setId(4);
        customer4.setName("이사");
        customer4.setAddress("대전시");
        customer4.setEmail("lee4@gmail.com");
        customers.add(customer4);
        Customer customer5 = new Customer();
        customer5.setId(5);
        customer5.setName("이오");
        customer5.setAddress("대구시");
        customer5.setEmail("lee5@gmail.com");
        customers.add(customer5);
    }

    @Override
    public Customer findById(long id) {
        for(Customer customer : customers) {
            if(customer.getId() == id)
                return customer;
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
        for(Customer customer : customers) {
            if(customer.getName().equals(name)) {
                Customer cust = new Customer();
                cust.setId(customer.getId());
                cust.setName(customer.getName());
                cust.setAddress(customer.getAddress());
                cust.setEmail(customer.getEmail());
                list.add(cust);
            }
        }
        return list;
    }
    @Override
    public void save(Customer customer) {
        for(int i = 0; i < customers.size(); i++) {
            if(customer.getId() == customers.get(i).getId()) {
                customers.set(i, customer);
                return;
            }
        }
        customers.add(customer);
    }

    @Override
    public void delete(long id) {
        Customer customer = findById(id);
        if(customer != null)
            customers.remove(customer);
    }
}