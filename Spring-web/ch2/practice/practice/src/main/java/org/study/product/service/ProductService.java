package org.study.product.service;

import org.study.domain.Product;

import java.util.List;

public interface ProductService {
    Product getProduct(long id);
    List<Product> getProducts();
    void saveProduct(Product product);
    void deleteProduct(long id);
}
