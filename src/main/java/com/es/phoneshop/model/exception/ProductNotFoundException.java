package com.es.phoneshop.model.exception;

public class ProductNotFoundException extends ItemNotFoundException {
    public ProductNotFoundException(Long id) {
        super(id);
    }
}
