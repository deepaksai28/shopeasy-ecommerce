package com.deepak.ecommerce.controller;

import com.deepak.ecommerce.model.CartItem;
import com.deepak.ecommerce.model.Order;
import com.deepak.ecommerce.model.OrderItem;
import com.deepak.ecommerce.repository.OrderRepository;
import com.deepak.ecommerce.service.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;

@Controller
public class OrderController {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // GET /checkout -> show the checkout form (name, email, address)
    @GetMapping("/checkout")
    public String checkoutForm(Model model, HttpSession session) {
        Cart cart = getCart(session);
        if (cart.getItemCount() == 0) {
            return "redirect:/cart"; // don't let people checkout an empty cart
        }
        model.addAttribute("cart", cart);
        return "checkout";
    }

    // POST /checkout -> process the form: convert Cart (session) into a permanent Order (database)
    @PostMapping("/checkout")
    public String placeOrder(@RequestParam String name,
                              @RequestParam String email,
                              @RequestParam String address,
                              HttpSession session,
                              Model model) {
        Cart cart = getCart(session);

        Order order = new Order();
        order.setCustomerName(name);
        order.setCustomerEmail(email);
        order.setAddress(address);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(cart.getTotal());

        // This is the key conversion step: each temporary CartItem becomes
        // a permanent OrderItem, snapshotting the product name and price
        // at the moment of purchase (see OrderItem.java comment for why).
        for (CartItem cartItem : cart.getItems().values()) {
            OrderItem orderItem = new OrderItem(
                    cartItem.getProduct().getName(),
                    cartItem.getProduct().getPrice(),
                    cartItem.getQuantity()
            );
            order.addItem(orderItem);
        }

        Order savedOrder = orderRepository.save(order); // cascade = ALL saves the OrderItems too
        cart.clear(); // empty the cart now that it's been converted to a permanent order

        model.addAttribute("order", savedOrder);
        return "order-confirmation";
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
