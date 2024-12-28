package org.study.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericGroovyApplicationContext;

public class Groovy_Main {
    public static void main(String[] args) {
        ApplicationContext context = new GenericGroovyApplicationContext("beans.groovy");
    }
}
