package com.es.phoneshop.model.exception;

public class OrderNotFoundException extends ItemNotFoundException {
    private String secureId;

    public OrderNotFoundException(Long id) {
        super(id);
    }

    public OrderNotFoundException(String secureId) {
        this.secureId = secureId;
    }

    public OrderNotFoundException() {
        super();
    }
}
