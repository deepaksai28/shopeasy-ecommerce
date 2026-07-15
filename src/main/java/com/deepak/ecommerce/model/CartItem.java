package com.deepak.ecommerce.model;

/**
 * Represents one line in the shopping cart.
 *
 * NOTE: This is NOT a @Entity - it's never saved to the database.
 * The cart lives only in the user's HTTP session (see CartController).
 * It only becomes permanent data (an Order + OrderItems) at checkout.
 * This mirrors how most real e-commerce sites work: your cart is
 * temporary/session state, your order history is permanent.
 */
public class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getSubtotal() { return product.getPrice() * quantity; }
}
