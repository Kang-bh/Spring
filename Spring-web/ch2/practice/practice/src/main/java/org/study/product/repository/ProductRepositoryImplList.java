package org.study.product.repository;

import org.springframework.stereotype.Repository;
import org.study.domain.Product;

import java.util.ArrayList;
import java.util.List;

@Repository("productRepository")
public class ProductRepositoryImplList implements ProductRepository{
    private List<Product> products;

    public ProductRepositoryImplList() {
        products = new ArrayList<Product>();
        Product product1 = new Product();
        product1.setId(1);
        product1.setName("제품1");
        product1.setDescription("제품1설명");
        product1.setPrice(10000);
        products.add(product1);
        Product product2 = new Product();
        product2.setId(2);
        product2.setName("제품2");
        product2.setDescription("제품2설명");
        product2.setPrice(20000);
        products.add(product2);
        Product product3 = new Product();
        product3.setId(3);
        product3.setName("제품3");
        product3.setDescription("제품3설명");
        product3.setPrice(30000);
        products.add(product3);
        Product product4 = new Product();
        product4.setId(4);
        product4.setName("제품4");
        product4.setDescription("제품4설명");
        product4.setPrice(40000);
        products.add(product4);
        Product product5 = new Product();
        product5.setId(5);
        product5.setName("제품5");
        product5.setDescription("제품5설명");
        product5.setPrice(50000);
        products.add(product5);
    }
    @Override
    public Product findById(long id) {
        for(Product product : products) {
            if(product.getId() == id)
                return product;
        }
        return null;
    }

    @Override
    public List<Product> findAll() {
        return products;
    }

    @Override
    public void save(Product product) {
        for(int i = 0; i < products.size(); i++) {
            if(product.getId() == products.get(i).getId()) {
                products.set(i, product);
                return;
            }
        }
        products.add(product);
    }

    @Override
    public void delete(long id) {
        Product product = findById(id);
        if(product != null)
            products.remove(product);
    }
}
