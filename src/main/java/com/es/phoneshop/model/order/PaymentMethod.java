package com.es.phoneshop.model.order;

public enum PaymentMethod {
    CASH("Cash"),
    CREDIT_CARD("Credit card");

    private final String value;

    PaymentMethod(String s) {
        value = s;
    }

    public String getValue() {
        return value;
    }

    public static PaymentMethod getEnum(String value) {
        for (PaymentMethod v : values()) {
            if (v.getValue().equalsIgnoreCase(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }
}
