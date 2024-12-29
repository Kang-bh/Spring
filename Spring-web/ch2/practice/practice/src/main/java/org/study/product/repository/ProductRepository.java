package org.study.product.repository;

import org.study.domain.Product;

import java.util.List;

public interface ProductRepository {
    Product findById(long id);
    List<Product> findAll();
    void save(Product product);
    void delete(long id);
}
