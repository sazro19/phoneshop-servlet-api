package com.es.phoneshop.model.advancedSearch;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        List<Product> result = productDao.findProducts();
        return result.stream()
                .filter(product -> {
                    Stream<String> words = Arrays.stream(description.trim().split("\\s+"));
                    if (option.equals(DescriptionOptions.ALL_WORDS)) {
                        return words.allMatch(word -> Arrays.asList(product.getDescription()
                                .split(" ")).contains(word));
                    } else {
                        return words.anyMatch(word -> Arrays.asList(product.getDescription()
                                .split(" ")).contains(word));
                    }
                })
                .filter(product -> {
                    if (minPrice != null) {
                        return product.getPrice().compareTo(minPrice) >= 0;
                    } else {
                        return true;
                    }
                })
                .filter(product -> {
                    if (maxPrice != null) {
                        return product.getPrice().compareTo(maxPrice) <= 0;
                    } else {
                        return true;
                    }
                }).collect(Collectors.toList());
    }

    private boolean allProductsRequired(String description, BigDecimal minPrice, BigDecimal maxPrice) {
        return (description == null || description.isEmpty()) && (minPrice == null) && (maxPrice == null);
    }
}
