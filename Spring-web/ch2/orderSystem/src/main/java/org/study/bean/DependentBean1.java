package org.study.bean;

public class DependentBean1 {
    private InjectedBean injectedBean;

    // with constructgor
    public DependentBean1(InjectedBean injectedBean) {
        System.out.println("DependentBean1 is constructing");
        System.out.println("InjectedBean is injecting to DependentBean1");
        this.injectedBean = injectedBean;
    }
}
