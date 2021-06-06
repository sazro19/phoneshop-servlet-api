package com.es.phoneshop.model.product.viewed;

import javax.servlet.http.HttpServletRequest;

public interface RecentlyViewedService {
    RecentlyViewedContainer getContainer(HttpServletRequest request);
    void add(RecentlyViewedContainer container, Long productId);
}
