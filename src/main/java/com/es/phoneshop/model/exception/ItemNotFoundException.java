package com.es.phoneshop.model.exception;

public class ItemNotFoundException extends RuntimeException {
    protected Long id;

    public ItemNotFoundException(Long id) {
        super("Item not found: " + id);
        this.id = id;
    }

    public ItemNotFoundException() {
        super();
    }

    public Long getId() {
        return id;
    }
}
