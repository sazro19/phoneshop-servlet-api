package com.es.phoneshop.model.product.exception;

public class ItemNotFoundException extends RuntimeException {
    protected Long id;

    public ItemNotFoundException(Long id) {
        this.id = id;
    }

    public ItemNotFoundException() {
    }

    public Long getId() {
        return id;
    }
}
