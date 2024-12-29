package org.study.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericGroovyApplicationContext;
import org.study.bean.PrototypeBean;
import org.study.bean.SingletonBean;
import org.study.ordersystem.Customer;
import org.study.ordersystem.CustomerService;

import java.util.List;

public class Groovy_Main {
    public static void main(String[] args) {
//        ApplicationContext context = new GenericGroovyApplicationContext("beans.groovy");
        ConfigurableApplicationContext context = new GenericGroovyApplicationContext("beans.groovy");
        context.registerShutdownHook();

        System.out.println("======= Scope =======");
        SingletonBean singletonBean1 = context.getBean("singletonBean", SingletonBean.class);
        PrototypeBean prototypeBean1 = context.getBean("prototypeBean", PrototypeBean.class);
        SingletonBean singletonBean2 = context.getBean("singletonBean", SingletonBean.class);
        PrototypeBean prototypeBean2 = context.getBean("prototypeBean", PrototypeBean.class);

        System.out.println("======= Customer =======");
        CustomerService customerService = context.getBean("customerService", CustomerService.class);
        List<Customer> customers = customerService.getCustomers();
        for(Customer customer : customers)
            System.out.println(customer);

        context.close();

    }
}
