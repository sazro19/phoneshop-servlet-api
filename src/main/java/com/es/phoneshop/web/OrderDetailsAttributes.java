package com.es.phoneshop.web;

public enum OrderDetailsAttributes {
    FIRSTNAME("firstname"),
    LASTNAME("lastname"),
    PHONE("phone"),
    DELIVERY_DATE("deliveryDate"),
    DELIVERY_ADDRESS("deliveryAddress"),
    PAYMENT_METHOD("paymentMethod");

    private String value;

    OrderDetailsAttributes(String v) {
        value = v;
    }

    public String getValue() {
        return value;
    }
}
