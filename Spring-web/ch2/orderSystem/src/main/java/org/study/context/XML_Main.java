package org.study.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.study.ordersystem.Customer;
import org.study.ordersystem.CustomerService;

import java.util.List;

public class XML_Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        CustomerService customerService = context.getBean("customerService", CustomerService.class);
        List<Customer> customers = customerService.getCustomers();
        for(Customer customer : customers)
            System.out.println(customer);
    }
}
