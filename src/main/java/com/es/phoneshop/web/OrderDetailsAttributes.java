package com.es.phoneshop.web;

public class OrderDetailsAttributes {
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String PHONE = "phone";
    private static final String DELIVERY_DATE = "deliveryDate";
    private static final String DELIVERY_ADDRESS = "deliveryAddress";
    private static final String PAYMENT_METHOD = "paymentMethod";

    private OrderDetailsAttributes() {
    }

    public static String getFIRSTNAME() {
        return FIRSTNAME;
    }

    public static String getLASTNAME() {
        return LASTNAME;
    }

    public static String getPHONE() {
        return PHONE;
    }

    public static String getDeliveryDate() {
        return DELIVERY_DATE;
    }

    public static String getDeliveryAddress() {
        return DELIVERY_ADDRESS;
    }

    public static String getPaymentMethod() {
        return PAYMENT_METHOD;
    }
}
