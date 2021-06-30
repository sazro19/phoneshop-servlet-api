package com.es.phoneshop.model.advancedSearch;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

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
    public List<Product> findProducts() {
        return null;
    }
}
