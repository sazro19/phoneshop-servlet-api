package com.es.phoneshop.model.product.order;

import com.es.phoneshop.model.product.cart.Cart;

import java.util.List;

public interface OrderService {
    Order getOrder(Cart cart);
    List<String> getStringPaymentMethods();
    void placeOrder(Order order);
    boolean isPhoneCorrect(String phone);
}
