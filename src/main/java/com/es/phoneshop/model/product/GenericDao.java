package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.exception.ItemNotFoundException;
import com.es.phoneshop.model.product.order.ArrayListOrderDao;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class GenericDao<T extends BaseItem> {
    protected List<T> itemList;

    protected long newId;

    protected static final ReentrantReadWriteLock lock;

    protected static final Logger LOGGER;

    static {
        lock = new ReentrantReadWriteLock(true);
        LOGGER = Logger.getLogger(ArrayListOrderDao.class.getName());
    }

    public T getItem(Long id) {
        lock.readLock().lock();
        try {
            if (id == null) {
                LOGGER.log(Level.WARNING, "getItem(Long id) has got null id");
                throw new ItemNotFoundException(id);
            }
            return itemList.stream()
                    .filter(item -> id.equals(item.getId()))
                    .findAny()
                    .orElseThrow(() -> new ItemNotFoundException(id));
        } finally {
            lock.readLock().unlock();
        }
    }

    public void save(T item) {
        lock.writeLock().lock();
        try {
            if (item == null) {
                throw new ItemNotFoundException();
            }
            if (item.getId() != null) {
                itemList.removeIf(o -> item.getId().equals(o.getId()));
                itemList.add(item);
                return;
            }
            item.setId(++newId);
            itemList.add(item);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
