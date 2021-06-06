package com.es.phoneshop.model.product.viewed;

import com.es.phoneshop.model.product.Product;

import java.util.*;

public class RecentlyViewedContainer {
    private Deque<Product> threeLastProducts;

    public RecentlyViewedContainer() {
        this.threeLastProducts = new ArrayDeque<>();
    }

    public void addProduct(Product product) {
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
        if (threeLastProducts.size() == 3) {
            threeLastProducts.removeLast();
            threeLastProducts.addFirst(product);
            return;
        }
        threeLastProducts.addFirst(product);
    }

    public Deque<Product> getThreeLastProducts() {
        return threeLastProducts;
    }
}
