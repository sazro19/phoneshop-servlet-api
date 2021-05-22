package com.es.phoneshop.model.product;

import com.es.phoneshop.data.DataGenerator;
import com.es.phoneshop.model.product.exception.ProductNotFoundException;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {

    private List<Product> productList;

    private long newId;

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    public ArrayListProductDao() {
        this.productList = DataGenerator.generateSampleProducts();
        if (productList.size() != 0) {
            newId = productList.get(productList.size() - 1).getId();
        }
    }

    @Override
    public Product getProduct(Long id) throws ProductNotFoundException {
        lock.readLock().lock();
        try {
            return productList.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(ProductNotFoundException::new);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts() {
        lock.readLock().lock();
        try {
            return productList.stream()
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
                productList.set(productList.indexOf(getProduct(product.getId())), product);
                return;
            }
            product.setId(++newId);
            productList.add(product);
        } catch (ProductNotFoundException ignored) {

        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void delete(Long id) {
        lock.writeLock().lock();
        try {
            productList.remove(getProduct(id));
        } catch (ProductNotFoundException ignored) {
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
}
