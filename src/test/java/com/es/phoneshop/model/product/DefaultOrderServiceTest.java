package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartItem;
import com.es.phoneshop.model.product.cart.exception.OutOfStockException;
import com.es.phoneshop.model.product.order.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class DefaultOrderServiceTest {
    private OrderService orderService;
    private OrderDao orderDao;

    @Before
    public void setup() {
        orderService = DefaultOrderService.getInstance();
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Test
    public void testGetOrder() {
        Cart cart = new Cart();
        CartItem cartItem1 = new CartItem(new Product(), 5);
        CartItem cartItem2 = new CartItem(new Product(), 1);
        List<CartItem> itemList = new ArrayList<>(Arrays.asList(cartItem1, cartItem2));
        cart.setItems(itemList);
        cart.setTotalPrice(new BigDecimal(5));

        Order order = new Order();
        order.setItems(itemList);

        assertEquals(order.getItems(), orderService.getOrder(cart).getItems());
    }

    @Test
    public void testGetStringPaymentMethods() {
        List<String> expected = Arrays.stream(PaymentMethod.values())
                .map(PaymentMethod::getValue).collect(Collectors.toList());

        assertEquals(expected, orderService.getStringPaymentMethods());
    }

    @Test
    public void testPlaceOrder() {
        Order order = new Order();
        orderService.placeOrder(order);
        order.setSecureId("zxc");

        assertEquals(order, orderDao.getItemBySecureOrderId("zxc"));
    }

    @Test
    public void testIsPhoneCorrect() {
        String right = "+375111234567";
        String invalid = "+375111231251251";

        assertTrue(orderService.isPhoneCorrect(right));
        assertFalse(orderService.isPhoneCorrect(invalid));
    }
}


