package org.study.bean;

public class DependentBean2 {
    // using field
    private InjectedBean injectedBean;
    public DependentBean2() { // default constructor
        System.out.println("DependentBean2 is constructing");
    }

    public void setInjectedBean(InjectedBean injectedBean) {
        System.out.println("DependentBean2 is injecting by setter method");
        this.injectedBean = injectedBean;
    }
}
