package com.deepak.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.deepak.ecommerce.model.Product;
import com.deepak.ecommerce.repository.ProductRepository;

/**
 * Entry point of the application.
 *
 * WHY CommandLineRunner is used here:
 * Since we're using an in-memory H2 database, it resets every time the app
 * restarts. This bean seeds a few sample products on startup so the store
 * isn't empty when you demo it. In a real production app this would instead
 * be a migration script (e.g. Flyway) or an admin panel to add products.
 */
@SpringBootApplication
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

    @Bean
    CommandLineRunner seedData(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() == 0) {
                productRepository.save(new Product(null, "Wireless Mouse", "Ergonomic 2.4GHz wireless mouse", 699.0, 50, "https://placehold.co/300x300?text=Mouse"));
                productRepository.save(new Product(null, "Mechanical Keyboard", "RGB backlit mechanical keyboard", 2499.0, 30, "https://placehold.co/300x300?text=Keyboard"));
                productRepository.save(new Product(null, "USB-C Hub", "7-in-1 USB-C hub with HDMI", 1299.0, 40, "https://placehold.co/300x300?text=Hub"));
                productRepository.save(new Product(null, "Laptop Stand", "Adjustable aluminium laptop stand", 899.0, 25, "https://placehold.co/300x300?text=Stand"));
                productRepository.save(new Product(null, "Webcam 1080p", "Full HD webcam with autofocus", 1799.0, 20, "https://placehold.co/300x300?text=Webcam"));
                productRepository.save(new Product(null, "Desk Lamp", "LED desk lamp with USB charging port", 999.0, 35, "https://placehold.co/300x300?text=Lamp"));
            }
        };
    }
}
