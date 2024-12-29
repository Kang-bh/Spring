package org.study.bean;

public class FactoryBean {
    private FactoryBean() {
        System.out.println("정적 팩토리 빈이 생성됩니다.");
    }

    private static class Factory {
        static final FactoryBean instance = new FactoryBean();
    }

    public static FactoryBean getInstance() {
        return Factory.instance;
    }

    public void destroy() {
        System.out.println("팩토리 빈을 정리합니다.");
    }
}
