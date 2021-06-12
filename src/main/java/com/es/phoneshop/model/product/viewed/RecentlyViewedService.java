package com.es.phoneshop.model.product.viewed;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpSession;
import java.util.Deque;

public interface RecentlyViewedService {
    RecentlyViewedContainer getContainer(HttpSession session);
    void add(RecentlyViewedContainer container, Long productId);
    Deque<Product> getThreeRecentlyViewedProducts(RecentlyViewedContainer container);
}
