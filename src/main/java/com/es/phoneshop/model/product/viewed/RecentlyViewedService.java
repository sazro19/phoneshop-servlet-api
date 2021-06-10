package com.es.phoneshop.model.product.viewed;

import javax.servlet.http.HttpSession;

public interface RecentlyViewedService {
    RecentlyViewedContainer getContainer(HttpSession session);
    void add(RecentlyViewedContainer container, Long productId);
}
