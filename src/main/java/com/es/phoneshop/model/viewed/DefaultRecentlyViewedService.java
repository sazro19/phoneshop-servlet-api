package com.es.phoneshop.model.viewed;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.lock.SessionLock;

import javax.servlet.http.HttpSession;
import java.util.Deque;
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
        Object sessionLock = SessionLock.getSessionLock(session, null);
        synchronized (sessionLock){
            RecentlyViewedContainer container = (RecentlyViewedContainer) session
                    .getAttribute(RECENTLY_VIEWED_SESSION_ATTRIBUTE);
            if (container == null) {
                session.setAttribute(RECENTLY_VIEWED_SESSION_ATTRIBUTE, container = new RecentlyViewedContainer());
            }
            return container;
        }
    }

    @Override
    public void add(RecentlyViewedContainer container, Long productId) {
        lock.writeLock().lock();
        try {
            Product product = productDao.getItem(productId);
            container.addProduct(product);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Deque<Product> getThreeRecentlyViewedProducts(RecentlyViewedContainer container) {
        lock.readLock().lock();
        try {
            return container.getThreeLastProducts();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Deque<Product> getOldThreeRecentlyViewedProducts(RecentlyViewedContainer container) {
        lock.readLock().lock();
        try {
            return container.getOldThreeLstProducts();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public String getLastPage(RecentlyViewedContainer container) {
        lock.readLock().lock();
        try {
            return container.getLastPage();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void setLastPage(RecentlyViewedContainer container, String value) {
        lock.writeLock().lock();
        try {
            container.setLastPage(value);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
