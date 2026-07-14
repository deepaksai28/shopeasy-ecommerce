package com.deepak.ecommerce.controller;

import com.deepak.ecommerce.model.Product;
import com.deepak.ecommerce.repository.ProductRepository;
import com.deepak.ecommerce.service.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.servlet.http.HttpSession;

/**
 * Handles pages for browsing the product catalog.
 *
 * @Controller (not @RestController) because these methods return the NAME
 * of an HTML template (Thymeleaf view) to render, not raw JSON/data.
 */
@Controller
public class ProductController {

    private final ProductRepository productRepository;

    @Autowired
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // GET / -> show all products (the homepage / storefront)
    @GetMapping("/")
    public String listProducts(Model model, HttpSession session) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("cartCount", getCart(session).getItemCount());
        return "index"; // resolves to templates/index.html
    }

    // GET /product/3 -> show detail page for one product
    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model, HttpSession session) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
        model.addAttribute("product", product);
        model.addAttribute("cartCount", getCart(session).getItemCount());
        return "product-detail";
    }

    // Helper: fetch the Cart object out of the session, creating one if it doesn't exist yet.
    private Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
}
