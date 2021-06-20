package com.es.phoneshop.model.product.order;

import com.es.phoneshop.model.product.GenericDao;
import com.es.phoneshop.model.product.exception.ItemNotFoundException;
import com.es.phoneshop.model.product.exception.OrderNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArrayListOrderDao extends GenericDao<Order> implements OrderDao {
    private static OrderDao instance;

    public static synchronized OrderDao getInstance() {
        if (instance == null) {
            instance = new ArrayListOrderDao();
        }
        return instance;
    }

    @Override
    public Order getItemBySecureOrderId(String secureOrderId) {
        lock.readLock().lock();
        try {
            if (secureOrderId == null) {
                LOGGER.log(Level.WARNING, "getItemBySecureOrderId(String secureOrderId) has got null id");
                throw new OrderNotFoundException();
            }
            return itemList.stream()
                    .filter(item -> secureOrderId.equals(item.getSecureId()))
                    .findAny()
                    .orElseThrow(() -> new OrderNotFoundException(secureOrderId));
        } finally {
            lock.readLock().unlock();
        }
    }

    private ArrayListOrderDao() {
        itemList = new ArrayList<>();
    }
}

