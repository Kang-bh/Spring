package org.study.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericGroovyApplicationContext;
import org.study.bean.*;
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
        FactoryBean factoryBean1 = context.getBean("factoryBean", FactoryBean.class);
        FactoryBean factoryBean2 = context.getBean("factoryBean", FactoryBean.class);

        System.out.println("======= DI =======");
        DependentBean1 dependentBean1 = context.getBean("dependentBean1", DependentBean1.class);
        DependentBean2 dependentBean2 = context.getBean("dependentBean2", DependentBean2.class);

        System.out.println("======= Customer =======");
        CustomerService customerService = context.getBean("customerService", CustomerService.class);
        List<Customer> customers = customerService.getCustomers();
        for(Customer customer : customers)
            System.out.println(customer);

        context.close();

    }
}
