package org.study.inventory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.study.domain.Inventory;
import org.study.domain.Product;
import org.study.inventory.repository.InventoryRepository;
import org.study.product.service.ProductService;

import java.util.ArrayList;
import java.util.List;

@Service("inventoryService")
public class InventoryServiceImpl implements InventoryService {
    private InventoryRepository inventoryRepository;
    private ProductService productService;
    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository, ProductService productService) {
        this.inventoryRepository = inventoryRepository;
        this.productService = productService;
    }
    @Override
    public Inventory getInventory(long id) {
        Inventory inventory = new Inventory();
        Product product = productService.getProduct(id);
        inventory.setId(id);
        inventory.setName(product.getName());
        inventory.setPrice(product.getPrice());
        inventory.setQuantity(inventoryRepository.findById(id));
        return inventory;
    }

    @Override
    public List<Inventory> getInventories() {
        List<Inventory> inventories = new ArrayList<>();
        List<Product> products = productService.getProducts();
        for(Product product : products) {
            Inventory inventory = new Inventory();
            inventory.setId(product.getId());
            inventory.setName(product.getName());
            inventory.setPrice(product.getPrice());
            inventory.setQuantity(inventoryRepository.findById(product.getId()));
            inventories.add(inventory);
        }
        return inventories;
    }

    @Override
    public void stockInventory(long id, long quantity) {
        long inventory = inventoryRepository.findById(id);
        inventoryRepository.save(id, inventory + quantity);
    }

    @Override
    public void takeInventory(long id, long quantity) {
        long inventory = inventoryRepository.findById(id);
        if(inventory >= quantity)
            inventoryRepository.save(id, inventory - quantity);
    }
}
