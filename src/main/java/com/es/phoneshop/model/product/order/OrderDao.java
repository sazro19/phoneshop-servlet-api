package com.es.phoneshop.model.product.order;

import com.es.phoneshop.model.product.exception.OrderNotFoundException;

public interface OrderDao {
    Order getItem(Long id) throws OrderNotFoundException;
    void save(Order order);
}
