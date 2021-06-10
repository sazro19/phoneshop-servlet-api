package com.es.phoneshop.model.product.viewed;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpSession;
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
    public RecentlyViewedContainer getContainer(HttpSession session) {
        lock.writeLock().lock();
        try {
            RecentlyViewedContainer container = (RecentlyViewedContainer) session
                    .getAttribute(RECENTLY_VIEWED_SESSION_ATTRIBUTE);
            if (container == null) {
                session.setAttribute(RECENTLY_VIEWED_SESSION_ATTRIBUTE, container = new RecentlyViewedContainer());
            }
            return container;
        } finally {
            lock.writeLock().unlock();
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
