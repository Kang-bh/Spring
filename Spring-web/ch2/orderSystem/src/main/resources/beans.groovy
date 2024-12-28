beans {
    customerRepository(org.study.ordersystem.CustomerRepositoryImpl) {}
    customerService(org.study.ordersystem.CustomerServiceImpl) {
        customerRepository = customerRepository
    }
}