package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.cart.exception.OutOfStockException;

import javax.servlet.http.HttpSession;

public interface CartService {
    Cart getCart(HttpSession session);
    void add(Cart cart, Long productId, int quantity) throws OutOfStockException;
    void update(Cart cart, Long productId, int quantity) throws OutOfStockException;
    void delete(Cart cart, Long productId);
    void clearCart(Cart cart);
}
