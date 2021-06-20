package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.exception.ProductNotFoundException;

import java.util.List;

public interface ProductDao {
    Product getItem(Long id) throws ProductNotFoundException;
    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);
    List<Product> findProducts(String query);
    List<Product> findProducts();
    void save(Product product);
    void delete(Long id) throws ProductNotFoundException;
}
