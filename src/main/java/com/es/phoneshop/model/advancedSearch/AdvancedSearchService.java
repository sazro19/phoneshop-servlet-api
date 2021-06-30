package com.es.phoneshop.model.advancedSearch;

import com.es.phoneshop.model.product.Product;

import java.math.BigDecimal;
import java.util.List;

public interface AdvancedSearchService {
    List<Product> findProducts(String description, BigDecimal minPrice, BigDecimal maxPrice, DescriptionOptions option);
}
