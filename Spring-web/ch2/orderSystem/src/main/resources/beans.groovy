beans {
    customerRepository(org.study.ordersystem.CustomerRepositoryImpl) {}
    customerService(org.study.ordersystem.CustomerServiceImpl) {
        customerRepository = customerRepository
    }
    singletonBean(org.study.bean.SingletonBean){ bean ->
        bean.initMethod = 'init'
        bean.destroyMethod = 'destroy'
    }
    prototypeBean(org.study.bean.PrototypeBean){ bean ->
        bean.scope = 'prototype'
        bean.initMethod = 'init'
        bean.destroyMethod = 'destroy'
    }
    factoryBean(org.study.bean.FactoryBean){ bean ->
        bean.destroyMethod = 'destroy'
    }
    injectedBean(org.study.bean.InjectedBean){ bean ->
        bean.scope = 'prototype'
    }
    dependentBean1(org.study.bean.DependentBean1, ref('injectedBean')){}
    dependentBean2(org.study.bean.DependentBean2){
        injectedBean = ref('injectedBean')
    }

}