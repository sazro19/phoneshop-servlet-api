package com.es.phoneshop.model.product.order;

import com.es.phoneshop.model.product.cart.Cart;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Order getOrder(Cart cart);
    List<String> getStringPaymentMethods();
    void placeOrder(Order order);
    void checkFirstname(Order order, String firstname, Map<String, String> errors);
    void checkLastname(Order order, String lastname, Map<String, String> errors);
    void checkDeliveryAddress(Order order, String address, Map<String, String> errors);
    void checkDeliveryDate(Order order, String date, Map<String, String> errors);
    void checkPaymentMethod(Order order, String paymentMethod, Map<String, String> errors);
    void checkPhone(Order order, String phone, Map<String, String> errors);
    Order getOrderBySecureId(String secureId);
}
