package org.study.context;

import groovy.util.Factory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.study.bean.PrototypeBean;
import org.study.bean.SingletonBean;
import org.study.bean.FactoryBean;
import org.study.ordersystem.CustomerRepository;
import org.study.ordersystem.CustomerRepositoryImpl;
import org.study.ordersystem.CustomerService;
import org.study.ordersystem.CustomerServiceImpl;

@Configuration
public class AppConfig {
    @Bean(value = "customerRepository") // @Bean("customerRepository")
    public CustomerRepository customerRepository() {
        return new CustomerRepositoryImpl();
    }

    @Bean(value = "customerService")
    public CustomerService customerService() {
        CustomerServiceImpl customerService = new CustomerServiceImpl();
        customerService.setCustomerRepository(customerRepository());
        return customerService;
    }

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public SingletonBean singletonBean() {
        return new SingletonBean();
    }

    @Bean(initMethod = "init", destroyMethod = "destroy")
    @Scope(value = "prototype")
    public PrototypeBean prototypeBean() {
        return new PrototypeBean();
    }

    @Bean(destroyMethod = "destroy")
    public FactoryBean factoryBean() {
        return FactoryBean.getInstance();
    }
}
