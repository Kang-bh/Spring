package org.study.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.study.domain.Product;
import org.study.product.service.ProductService;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

@Component("productUI")
public class ProductUI {
    private Scanner scanner;
    private ProductService productService;
    @Autowired
    public ProductUI(ProductService productService) {
        this.productService = productService;
        scanner = new Scanner(System.in);
    }
    public void insertProduct() {
        System.out.println("제품 정보를 입력하세요: ");
        System.out.print("제품 ID: ");
        long id = scanner.nextLong();
        System.out.print("제품 이름: ");
        String name = scanner.next();
        System.out.print("제품 설명: ");
        String description = scanner.next();
        System.out.print("제품 가격: ");
        long price = scanner.nextLong();
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        productService.saveProduct(product);
    }
    public void getProduct() {
        System.out.print("제품 ID: ");
        long id = scanner.nextLong();
        Product product = productService.getProduct(id);
        System.out.println(product);
    }

    public void getProducts() {
        List<Product> products = productService.getProducts();
        for(Product c : products) {
            System.out.println(c);
        }
    }

    public void updateProduct() {
        System.out.print("제품 ID: ");
        long id = scanner.nextLong();
        Product old = productService.getProduct(id);
        System.out.println(old);
        System.out.println("변경 정보를 입력하세요: ");
        System.out.print("제품 이름: ");
        String name = scanner.next();
        System.out.print("제품 설명: ");
        String description = scanner.next();
        System.out.print("제품 가격: ");
        long price = scanner.nextLong();
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        productService.saveProduct(product);
    }

    public void deleteProduct() {
        System.out.print("제품 ID: ");
        long id = scanner.nextLong();
        Product old = productService.getProduct(id);
        System.out.println(old);
        System.out.print("삭제하겠습니까? (Y: 예, N: 아니오): ");
        String yesOrNo = scanner.next();
        if(yesOrNo.toLowerCase(Locale.KOREAN).equals("y"))
            productService.deleteProduct(id);
    }
    public void insertProductInfos() {
        Product product = new Product();
        product.setId(1);
        product.setName("제품1");
        product.setDescription("제품1설명");
        product.setPrice(10000);
        productService.saveProduct(product);
        product = new Product();
        product.setId(2);
        product.setName("제품2");
        product.setDescription("제품2설명");
        product.setPrice(20000);
        productService.saveProduct(product);
        product = new Product();
        product.setId(3);
        product.setName("제품3");
        product.setDescription("제품3설명");
        product.setPrice(30000);
        productService.saveProduct(product);
        getProducts();
    }
}
