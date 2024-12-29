package org.study.inventory.repository;

import org.springframework.stereotype.Repository;
import org.study.domain.Product;
import org.study.product.repository.ProductRepositoryImplList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository("inventoryRepository")
public class InventoryRepositoryImplList implements InventoryRepository {
    private Map<Long, Long> inventories;

    public InventoryRepositoryImplList() {
        inventories = new HashMap<Long, Long>();
        List<Product> products = new ProductRepositoryImplList().findAll();
        for(Product product : products) {
            long quantity = product.getId() * 10;
            inventories.put(product.getId(), quantity);
        }
    }

    @Override
    public long findById(long id) {
        Long value = inventories.get(id);
        return Optional.ofNullable(value).orElse(0L);

    }

    @Override
    public void save(long id, long quantity) {
        inventories.put(id, quantity);
    }
}
