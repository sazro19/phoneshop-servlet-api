package com.es.phoneshop.model.product.viewed;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.cart.Cart;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultRecentlyViewedService implements RecentlyViewedService{
    private static final String RECENTLY_VIEWED_SESSION_ATTRIBUTE = DefaultRecentlyViewedService.class.getName() + ".container";

    private ProductDao productDao;

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    private static class Singleton {
        private static final DefaultRecentlyViewedService INSTANCE = new DefaultRecentlyViewedService();
    }

    public static DefaultRecentlyViewedService getInstance() {
        return DefaultRecentlyViewedService.Singleton.INSTANCE;
    }

    private DefaultRecentlyViewedService() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public RecentlyViewedContainer getContainer(HttpServletRequest request) {
        lock.readLock().lock();
        try {
            RecentlyViewedContainer container = (RecentlyViewedContainer) request.getSession()
                    .getAttribute(RECENTLY_VIEWED_SESSION_ATTRIBUTE);
            if (container == null) {
                request.getSession()
                        .setAttribute(RECENTLY_VIEWED_SESSION_ATTRIBUTE, container = new RecentlyViewedContainer());
            }
            return container;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void add(RecentlyViewedContainer container, Long productId) {
        lock.writeLock().lock();
        try {
            Product product = productDao.getProduct(productId);
            container.addProduct(product);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
