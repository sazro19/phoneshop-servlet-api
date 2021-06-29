package com.es.phoneshop.model.viewed;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpSession;
import java.util.Deque;

public interface RecentlyViewedService {
    RecentlyViewedContainer getContainer(HttpSession session);
    void add(RecentlyViewedContainer container, Long productId);
    Deque<Product> getThreeRecentlyViewedProducts(RecentlyViewedContainer container);
    Deque<Product> getOldThreeRecentlyViewedProducts(RecentlyViewedContainer container);
    String getLastPage(RecentlyViewedContainer container);
    void setLastPage(RecentlyViewedContainer container, String value);
}
