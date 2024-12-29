package org.study.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.study.domain.Product;
import org.study.inventory.service.InventoryService;
import org.study.product.service.ProductService;

import java.util.List;
import java.util.Scanner;

@Component("inventoryUI")
public class InventoryUI {
    private Scanner scanner;
    private InventoryService inventoryService;
    private ProductService productService;
    @Autowired
    public InventoryUI(InventoryService inventoryService, ProductService productService) {
        this.inventoryService = inventoryService;
        this.productService = productService;
        scanner = new Scanner(System.in);
    }
    public void storeProduct() {
        System.out.println("제품을 선택하세요: ");
        System.out.print("제품 ID: ");
        long id = scanner.nextLong();
        Product product = productService.getProduct(id);
        System.out.println(product);
        System.out.print("수량: ");
        long quantity = scanner.nextLong();
        inventoryService.stockInventory(id, quantity);
    }

    public void getInventory() {
        System.out.println("제품을 선택하세요: ");
        System.out.print("제품 ID: ");
        long id = scanner.nextLong();
        Product product = productService.getProduct(id);
        System.out.println(product);
        long quantity = inventoryService.getInventory(id).getQuantity();
        System.out.println("재고 수량: " + quantity);
    }

    public void getInventories() {
        List<Product> products = productService.getProducts();
        for(Product product : products) {
            System.out.print(product);
            long quantity = inventoryService.getInventory(product.getId()).getQuantity();
            System.out.println(" 재고 수량 : " + quantity);
        }
    }

    public void takeInventory() {
        System.out.println("제품을 선택하세요: ");
        System.out.print("제품 ID: ");
        long id = scanner.nextLong();
        Product product = productService.getProduct(id);
        System.out.print(product);
        long inventory = inventoryService.getInventory(product.getId()).getQuantity();
        System.out.println(" 재고 수량 : " + inventory);
        long quantity = scanner.nextLong();
        if(inventory < quantity) {
            System.out.println("재고가 부족합니다. 출고할 수 없습니다.");
            return;
        }
        inventoryService.takeInventory(id, quantity);
    }

    public void storeInvetoryInfos() {
        List<Product> products = productService.getProducts();
        for(Product product : products) {
            System.out.print(product);
            long quantity = product.getId() * 10;
            System.out.println(" 수량 : " + quantity);
            inventoryService.stockInventory(product.getId(), quantity);
        }
    }
}
