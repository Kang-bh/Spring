package org.study;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.study.ordersystem.Customer;
import org.study.ordersystem.CustomerService;

import java.util.List;

@Configuration
@ComponentScan(basePackages = {"org.study.ordersystem"})
public class App 
{
    public static void main( String[] args )
    {
        ApplicationContext context = new AnnotationConfigApplicationContext("AppConfig.class");


    }
}
