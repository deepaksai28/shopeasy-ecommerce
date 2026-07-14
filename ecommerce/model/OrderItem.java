package com.deepak.ecommerce.model;

import jakarta.persistence.*;

/**
 * A single line item inside an order (e.g. "2x Wireless Mouse").
 *
 * WHY we store productName/priceAtPurchase here instead of only a
 * reference to Product: if the product's price changes later, or the
 * product is deleted, the order history must still show what the
 * customer actually paid at the time. This is a common real-world
 * e-commerce pattern called "snapshotting".
 */
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private Double priceAtPurchase;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderItem() {}

    public OrderItem(String productName, Double priceAtPurchase, Integer quantity) {
        this.productName = productName;
        this.priceAtPurchase = priceAtPurchase;
        this.quantity = quantity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Double getPriceAtPurchase() { return priceAtPurchase; }
    public void setPriceAtPurchase(Double priceAtPurchase) { this.priceAtPurchase = priceAtPurchase; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Double getSubtotal() { return priceAtPurchase * quantity; }
}
