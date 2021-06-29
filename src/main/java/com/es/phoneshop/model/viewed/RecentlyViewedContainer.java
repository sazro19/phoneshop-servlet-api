package com.es.phoneshop.model.viewed;

import com.es.phoneshop.model.product.Product;

import java.util.*;

public class RecentlyViewedContainer {
    private Deque<Product> threeLastProducts;

    private Deque<Product> oldThreeLstProducts;

    private static final int MAX_RECENTLY_VIEWED = 3;

    private Long lastVisitedProductId;

    private String lastPage;

    public RecentlyViewedContainer() {
        this.threeLastProducts = new ArrayDeque<>();
        this.oldThreeLstProducts = new ArrayDeque<>();
    }

    protected void addProduct(Product product) {
        oldThreeLstProducts = getThreeLastProducts();
        if (threeLastProducts.contains(product)) {
            Product first = threeLastProducts.peek();
            if (first.equals(product)) {
                return;
            }
            Iterator<Product> iterator = threeLastProducts.iterator();
            while (iterator.hasNext()) {
                Product forChange = iterator.next();
                if (forChange.equals(product)) {
                    iterator.remove();
                    threeLastProducts.addFirst(forChange);
                    break;
                }
            }
            return;
        }
        if (threeLastProducts.size() == MAX_RECENTLY_VIEWED) {
            threeLastProducts.removeLast();
            threeLastProducts.addFirst(product);
            return;
        }
        threeLastProducts.addFirst(product);
    }

    protected Deque<Product> getThreeLastProducts() {
        return new ArrayDeque<>(threeLastProducts);
    }

    protected Deque<Product> getOldThreeLstProducts() {
        return new ArrayDeque<>(oldThreeLstProducts);
    }

    protected Long getLastVisitedProductId() {
        return this.lastVisitedProductId;
    }

    protected void setLastVisitedProductId(Long lastVisitedProductId) {
        this.lastVisitedProductId = lastVisitedProductId;
    }

    protected String getLastPage() {
        return lastPage;
    }

    protected void setLastPage(String lastPage) {
        this.lastPage = lastPage;
    }
}
