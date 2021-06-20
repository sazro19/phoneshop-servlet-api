package com.es.phoneshop.model.product.exception;

public class OrderNotFoundException extends ItemNotFoundException {
    public OrderNotFoundException(Long id) {
        super(id);
    }

    public OrderNotFoundException() {
        super();
    }
}
