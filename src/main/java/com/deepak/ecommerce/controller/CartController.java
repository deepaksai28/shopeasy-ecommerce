package com.deepak.ecommerce.controller;

import com.deepak.ecommerce.model.Product;
import com.deepak.ecommerce.repository.ProductRepository;
import com.deepak.ecommerce.service.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final ProductRepository productRepository;

    @Autowired
    public CartController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // GET /cart -> show the cart page
    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        Cart cart = getCart(session);
        model.addAttribute("cart", cart);
        model.addAttribute("cartCount", cart.getItemCount());
        return "cart";
    }

    // POST /cart/add/5?quantity=2 -> add product id 5, quantity 2, to cart
    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId,
                             @RequestParam(defaultValue = "1") int quantity,
                             HttpSession session) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
        getCart(session).addProduct(product, quantity);
        return "redirect:/cart"; // Post-Redirect-Get pattern: avoids re-submitting the form on refresh
    }

    // POST /cart/update/5?quantity=3 -> change quantity for an existing cart line
    @PostMapping("/update/{productId}")
    public String updateQuantity(@PathVariable Long productId,
                                  @RequestParam int quantity,
                                  HttpSession session) {
        getCart(session).updateQuantity(productId, quantity);
        return "redirect:/cart";
    }

    // POST /cart/remove/5 -> remove a line entirely
    @PostMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId, HttpSession session) {
        getCart(session).removeProduct(productId);
        return "redirect:/cart";
    }

    private Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
}
