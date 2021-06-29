package com.es.phoneshop.model.product;

import com.es.phoneshop.model.order.ArrayListOrderDao;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderDao;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayListOrderDaoTest {
    private OrderDao orderDao;

    @Before
    public void setup() {
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Test
    public void testGetItem() {
        Order order = new Order();
        orderDao.save(order);

        assertEquals(order.getId(), orderDao.getItem(order.getId()).getId());
    }

    @Test
    public void testGetItemBySecureId() {
        Order order = new Order();
        order.setSecureId("zxczxczxc");
        orderDao.save(order);

        assertEquals(order.getSecureId(), orderDao.getItemBySecureOrderId(order.getSecureId()).getSecureId());
    }
}
