package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.exception.ProductNotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private static ProductDao instance;

    public static synchronized ProductDao getInstance() {
        if (instance == null) {
            instance = new ArrayListProductDao();
        }
        return instance;
    }

    private List<Product> productList;

    private long newId;

    private static final ReentrantReadWriteLock lock;

    private static final Logger LOGGER;

    static {
        lock = new ReentrantReadWriteLock(true);
        LOGGER = Logger.getLogger(ArrayListProductDao.class.getName());
    }

    private ArrayListProductDao() {
        this.productList = new ArrayList<>();
    }

    @Override
    public Product getProduct(Long id) throws ProductNotFoundException {
        lock.readLock().lock();
        try {
            if (id == null) {
                LOGGER.log(Level.WARNING, "getProduct(Long id) has got null id");
                throw new ProductNotFoundException(id);
            }
            return productList.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundException(id));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        lock.readLock().lock();
        try {
            Comparator<Product> comparator;
            if (SortField.description == sortField) {
                comparator = Comparator.comparing(Product::getDescription);
            } else {
                comparator = Comparator.comparing(Product::getPrice);
            }
            if (SortOrder.desc == sortOrder) {
                comparator = comparator.reversed();
            }

            List<Product> result = productList.stream()
                    .filter(this::isProductNotNull)
                    .filter(this::isPriceNotNull)
                    .filter(this::isProductInStock)
                    .filter(p -> isProductMatchesAnyQuery(p, query))
                    .sorted((p1, p2) -> Long.compare(countOccurrence(p2, query), countOccurrence(p1, query)))
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
            List<Product> result = productList.stream()
                    .filter(this::isProductNotNull)
                    .filter(this::isPriceNotNull)
                    .filter(this::isProductInStock)
                    .filter(p -> isProductMatchesAnyQuery(p, query))
                    .sorted((p1, p2) -> Long.compare(countOccurrence(p2, query), countOccurrence(p1, query)))
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
            return productList.stream()
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
                if (productList.removeIf(p -> {
                    if (product.getId().equals(p.getId()) && !product.getPrice().equals(p.getPrice())) {
                        oldPrices.addAll(p.getPriceHistoryList());
                    }
                    return product.getId().equals(p.getId());
                })) {
                    product.getPriceHistoryList().addAll(oldPrices);
                }
                productList.add(product);
                return;
            }
            product.setId(++newId);
            productList.add(product);
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
            if (!productList.removeIf(product -> id.equals(product.getId()))) {
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
        List<String> words = new ArrayList<>(Arrays.asList(query.split(" ")));
        return words.stream()
                .anyMatch(word -> Arrays.asList(product.getDescription().split(" ")).contains(word));
    }

    private long countOccurrence(Product product, String query) {
        if (query == null || query.isEmpty()) {
            return 0;
        }
        List<String> words = new ArrayList<>(Arrays.asList(query.split(" ")));
        List<String> descriptionWords = new ArrayList<>(Arrays.asList(product.getDescription().split(" ")));
        return descriptionWords.stream()
                .filter(words::contains)
                .count();
    }
}

