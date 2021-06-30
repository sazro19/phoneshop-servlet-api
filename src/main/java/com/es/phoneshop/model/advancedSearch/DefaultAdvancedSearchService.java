package com.es.phoneshop.model.advancedSearch;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DefaultAdvancedSearchService implements AdvancedSearchService{
    private ProductDao productDao;

    private static class Singleton {
        private static final DefaultAdvancedSearchService INSTANCE = new DefaultAdvancedSearchService();
    }

    public static DefaultAdvancedSearchService getInstance() {
        return DefaultAdvancedSearchService.Singleton.INSTANCE;
    }

    private DefaultAdvancedSearchService() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public List<Product> findProducts(String description, BigDecimal minPrice,
                                      BigDecimal maxPrice,DescriptionOptions option) {
        if (allProductsRequired(description, minPrice, maxPrice)) {
            return new ArrayList<>(productDao.findProducts());
        }
        return new ArrayList<>();
    }

    private boolean allProductsRequired(String description, BigDecimal minPrice, BigDecimal maxPrice) {
        return (description == null || description.isEmpty()) && (minPrice == null) && (maxPrice == null);
    }
}
