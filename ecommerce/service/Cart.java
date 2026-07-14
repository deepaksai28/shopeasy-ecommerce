package com.deepak.ecommerce.service;

import com.deepak.ecommerce.model.CartItem;
import com.deepak.ecommerce.model.Product;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Holds the logic for manipulating a shopping cart.
 *
 * WHY this is a plain object stored in the session (not a Spring @Service
 * singleton bean holding the data): a @Service bean is shared by ALL users
 * of the app - if we stored cart items directly inside a @Service field,
 * every visitor would see the same shared cart. Instead, this class is
 * instantiated fresh per-session and stored as a session attribute
 * (see CartController). Session = "temporary storage tied to one browser".
 */
public class Cart implements Serializable {

    // Map key = productId, value = CartItem. LinkedHashMap keeps insertion order
    // so items appear in the cart in the order they were added.
    private final Map<Long, CartItem> items = new LinkedHashMap<>();

    public void addProduct(Product product, int quantity) {
        if (items.containsKey(product.getId())) {
            CartItem existing = items.get(product.getId());
            existing.setQuantity(existing.getQuantity() + quantity);
        } else {
            items.put(product.getId(), new CartItem(product, quantity));
        }
    }

    public void updateQuantity(Long productId, int quantity) {
        if (quantity <= 0) {
            items.remove(productId);
        } else if (items.containsKey(productId)) {
            items.get(productId).setQuantity(quantity);
        }
    }

    public void removeProduct(Long productId) {
        items.remove(productId);
    }

    public void clear() {
        items.clear();
    }

    public Map<Long, CartItem> getItems() {
        return items;
    }

    public double getTotal() {
        return items.values().stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    public int getItemCount() {
        return items.values().stream().mapToInt(CartItem::getQuantity).sum();
    }
}
