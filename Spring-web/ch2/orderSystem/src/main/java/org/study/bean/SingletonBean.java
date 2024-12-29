package org.study.bean;

public class SingletonBean {
    public SingletonBean() {
        System.out.println("싱글톤 스프링 빈이 생성됩니다...");;
    }

    public void init() {
        System.out.println("싱글톤 스프링 빈을 초기화합니다.");
    }

    public void destroy() {
        System.out.println("싱글톤 스프링 빈을 정리합니다.");
    }
}
