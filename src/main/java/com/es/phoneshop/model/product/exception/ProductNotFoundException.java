package com.es.phoneshop.model.product.exception;

public class ProductNotFoundException extends ItemNotFoundException {
    public ProductNotFoundException(Long id) {
        super(id);
    }
}
