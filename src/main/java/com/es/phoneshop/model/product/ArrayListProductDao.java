package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.exception.ProductNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayListProductDao extends GenericDao<Product> implements ProductDao {
    private static ProductDao instance;

    public static synchronized ProductDao getInstance() {
        if (instance == null) {
            instance = new ArrayListProductDao();
        }
        return instance;
    }

    private ArrayListProductDao() {
        this.itemList = new ArrayList<>();
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        lock.readLock().lock();
        try {
            Comparator<Product> comparator;
            if (SortField.DESCRIPTION == sortField) {
                comparator = Comparator.comparing(Product::getDescription);
            } else {
                comparator = Comparator.comparing(Product::getPrice);
            }
            if (SortOrder.DESC == sortOrder) {
                comparator = comparator.reversed();
            }

            List<Product> result = itemList.stream()
                    .filter(this::isProductNotNull)
                    .filter(this::isPriceNotNull)
                    .filter(this::isProductInStock)
                    .filter(p -> isProductMatchesAnyQuery(p, query))
                    .sorted((p1, p2) -> {
                        long secondOcc = countOccurrence(p2, query);
                        long firstOcc = countOccurrence(p1, query);
                        if (secondOcc == firstOcc) {
                            return Double.compare(calcOccurrencesWithDescription(p2, secondOcc),
                                    calcOccurrencesWithDescription(p1, firstOcc));
                        } else {
                            return Long.compare(secondOcc, firstOcc);
                        }
                    })
                    .collect(Collectors.toList());

            if (sortField != null) {
                result = result.stream()
                        .sorted(comparator)
                        .collect(Collectors.toList());
            }
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts(String query) {
        lock.readLock().lock();
        try {
            List<Product> result = itemList.stream()
                    .filter(this::isProductNotNull)
                    .filter(this::isPriceNotNull)
                    .filter(this::isProductInStock)
                    .filter(p -> isProductMatchesAnyQuery(p, query))
                    .sorted((p1, p2) -> {
                        long secondOcc = countOccurrence(p2, query);
                        long firstOcc = countOccurrence(p1, query);
                        if (secondOcc == firstOcc) {
                            return Double.compare(calcOccurrencesWithDescription(p2, secondOcc),
                                    calcOccurrencesWithDescription(p1, firstOcc));
                        } else {
                            return Long.compare(secondOcc, firstOcc);
                        }
                    })
                    .collect(Collectors.toList());
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts() {
        lock.readLock().lock();
        try {
            return itemList.stream()
                    .filter(this::isProductNotNull)
                    .filter(this::isPriceNotNull)
                    .filter(this::isProductInStock)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void save(Product product) {
        lock.writeLock().lock();
        try {
            if (product.getId() != null) {
                List<PriceHistory> oldPrices = new ArrayList<>();
                if (itemList.removeIf(p -> isProductForDeleting(product, p, oldPrices))) {
                    product.getPriceHistoryList().addAll(oldPrices);
                }
                itemList.add(product);
                return;
            }
            product.setId(++newId);
            itemList.add(product);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void delete(Long id) throws ProductNotFoundException {
        lock.writeLock().lock();
        try {
            if (id == null) {
                LOGGER.log(Level.WARNING, "delete(Long id) has got null id");
                throw new ProductNotFoundException(id);
            }
            if (!itemList.removeIf(product -> id.equals(product.getId()))) {
                throw new ProductNotFoundException(id);
            } else {
                newId--;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private boolean isProductInStock(Product product) {
        return product.getStock() > 0;
    }

    private boolean isPriceNotNull(Product product) {
        return product.getPrice() != null;
    }

    private boolean isProductNotNull(Product product) {
        return product != null;
    }

    private boolean isProductMatchesAnyQuery(Product product, String query) {
        if (query == null || query.isEmpty()) {
            return true;
        }
        Stream<String> words = Arrays.stream(query.trim().split("\\s+"));
        return words
                .anyMatch(word -> Arrays.asList(product.getDescription().split(" ")).contains(word));
    }

    private long countOccurrence(Product product, String query) {
        if (query == null || query.isEmpty()) {
            return 0;
        }
        List<String> words = new ArrayList<>(Arrays.asList(query.trim().split("\\s+")));
        List<String> descriptionWords = Arrays.asList(product.getDescription().split(" "));
        return descriptionWords.stream()
                .filter(words::contains)
                .count();
    }

    private boolean isUpdatedProductWithNewPrice(Product updated, Product inList) {
        return updated.getId().equals(inList.getId()) && !updated.getPrice().equals(inList.getPrice());
    }

    private boolean isProductForDeleting(Product updated, Product inList, List<PriceHistory> oldPrices) {
        if (isUpdatedProductWithNewPrice(updated, inList)) {
            oldPrices.addAll(inList.getPriceHistoryList());
        }
        return updated.getId().equals(inList.getId());
    }

    private double calcOccurrencesWithDescription(Product product, long occurrence) {
        List<String> descriptionWords = Arrays.asList(product.getDescription().split(" "));
        return (double) occurrence / descriptionWords.size();
    }
}

