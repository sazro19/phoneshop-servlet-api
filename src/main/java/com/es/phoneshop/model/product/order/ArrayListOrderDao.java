package com.es.phoneshop.model.product.order;

import com.es.phoneshop.model.product.GenericDao;
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

    private ArrayListOrderDao() {
        itemList = new ArrayList<>();
    }
}

