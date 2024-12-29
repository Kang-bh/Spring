package org.study.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.study.customer.service.CustomerService;
import org.study.domain.Customer;


import java.util.List;
import java.util.Locale;
import java.util.Scanner;

@Component("customerUI")
public class CustomerUI {
    private Scanner scanner;
    private CustomerService customerService;
    @Autowired
    public CustomerUI(CustomerService customerService) {
        this.customerService = customerService;
        scanner = new Scanner(System.in);
    }
    public void insertCustomer() {
        System.out.println("고객 정보를 입력하세요: ");
        System.out.print("고객 ID: ");
        long id = scanner.nextLong();
        System.out.print("고객 이름: ");
        String name = scanner.next();
        System.out.print("고객 주소: ");
        String address = scanner.next();
        System.out.print("고객 이메일: ");
        String email = scanner.next();
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName(name);
        customer.setAddress(address);
        customer.setEmail(email);
        customerService.saveCustomer(customer);
    }
    public void getCustomer() {
        System.out.print("고객 ID: ");
        long id = scanner.nextLong();
        Customer customer = customerService.getCustomer(id);
        System.out.println(customer);
    }

    public void getCustomers() {
        List<Customer> customers = customerService.getCustomers();
        for(Customer c : customers) {
            System.out.println(c);
        }
    }

    public void updateCustomer() {
        System.out.print("고객 ID: ");
        long id = scanner.nextLong();
        Customer old = customerService.getCustomer(id);
        System.out.println(old);
        System.out.println("변경 정보를 입력하세요: ");
        System.out.print("고객 이름: ");
        String name = scanner.next();
        System.out.print("고객 주소: ");
        String address = scanner.next();
        System.out.print("고객 이메일: ");
        String email = scanner.next();
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName(name);
        customer.setAddress(address);
        customer.setEmail(email);
        customerService.saveCustomer(customer);
    }

    public void deleteCustomer() {
        System.out.print("고객 ID: ");
        long id = scanner.nextLong();
        Customer old = customerService.getCustomer(id);
        System.out.println(old);
        System.out.print("삭제하겠습니까? (Y: 예, N: 아니오): ");
        String yesOrNo = scanner.next();
        if(yesOrNo.toLowerCase(Locale.KOREAN).equals("y"))
            customerService.deleteCustomer(id);
    }
    public void insertCustomerInfos() {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("김일");
        customer.setAddress("서울시");
        customer.setEmail("kim1@gmail.com");
        customerService.saveCustomer(customer);
        customer = new Customer();
        customer.setId(2);
        customer.setName("김이");
        customer.setAddress("대전시");
        customer.setEmail("kim2@gmail.com");
        customerService.saveCustomer(customer);
        customer = new Customer();
        customer.setId(3);
        customer.setName("김삼");
        customer.setAddress("부산시");
        customer.setEmail("kim3@gmail.com");
        customerService.saveCustomer(customer);
        getCustomers();
    }
}
