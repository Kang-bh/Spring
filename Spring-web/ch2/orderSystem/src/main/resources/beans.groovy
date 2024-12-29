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
        bean.initMethod = 'init'
        bean.destroyMethod = 'destroy'
    }
}