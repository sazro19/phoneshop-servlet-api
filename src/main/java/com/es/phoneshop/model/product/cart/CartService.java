package com.es.phoneshop.model.product.cart;

import com.es.phoneshop.model.product.cart.exception.OutOfStockException;

public interface CartService {
    Cart getCart();
    void add(Long productId, int quantity) throws OutOfStockException;
}
