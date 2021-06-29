package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.Cart;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface OrderService {
    Order getOrder(Cart cart);
    List<String> getStringPaymentMethods();
    void placeOrder(Order order);
    void checkDeliveryDate(Order order, String date, Map<String, String> errors);
    void checkPaymentMethod(Order order, String paymentMethod, Map<String, String> errors);
    void checkPhone(Order order, String phone, Map<String, String> errors);
    Order getOrderBySecureId(String secureId);

    void setOrderFieldOrErrors(String value, String field, Map<String, String> errors,
                               Consumer<String> consumer);
}
