package org.study.ordersystem;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class Main {


    // 스프링 Context 객체 생성(IoC Container)
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        CustomerService customerService = context.getBean("customerService", CustomerService.class);

        System.out.println("====== Test FindAll Function ======");
        List<Customer> customers = customerService.getCustomers();
        for(Customer customer : customers) {
            System.out.println(customer);
        }

        System.out.println("\n");
        System.out.println("====== Test Save Function ======");

        Customer newCustomer = new Customer();
        newCustomer.setId(6);
        newCustomer.setName("Customer #6");
        newCustomer.setEmail("customer_6@gmail.com");
        newCustomer.setAddress("Address 6");
        customerService.saveCustomer(newCustomer);

        Customer customer6 = customerService.getCustomer(6);
        System.out.println(customer6);

        System.out.println("\n");
        System.out.println("====== Test Delete Function ======");

        customerService.deleteCustomer(1);
        List<Customer> customerList = customerService.getCustomers();
        for(Customer customer : customerList) {
            System.out.println(customer);
        }
    }
}
