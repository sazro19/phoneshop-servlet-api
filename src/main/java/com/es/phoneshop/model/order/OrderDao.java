package com.es.phoneshop.model.order;

import com.es.phoneshop.model.exception.OrderNotFoundException;

public interface OrderDao {
    Order getItem(Long id) throws OrderNotFoundException;
    void save(Order order);
    Order getItemBySecureOrderId(String secureOrderId);
}
