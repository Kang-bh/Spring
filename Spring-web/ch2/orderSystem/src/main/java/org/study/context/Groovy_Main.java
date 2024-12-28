package org.study.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericGroovyApplicationContext;
import org.study.ordersystem.Customer;
import org.study.ordersystem.CustomerService;

import java.util.List;

public class Groovy_Main {
    public static void main(String[] args) {
        ApplicationContext context = new GenericGroovyApplicationContext("beans.groovy");
        CustomerService customerService = context.getBean("customerService", CustomerService.class);
        List<Customer> customers = customerService.getCustomers();
        for(Customer customer : customers)
            System.out.println(customer);
    }
}
