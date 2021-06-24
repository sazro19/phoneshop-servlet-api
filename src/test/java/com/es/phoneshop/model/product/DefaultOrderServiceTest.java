package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartItem;
import com.es.phoneshop.model.product.order.*;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
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
    public void testIsFirstnameCorrect() {
        String right = "Alex";
        String invalid = "";
        Order order = new Order();
        Map<String, String> errors = new HashMap<>();

        orderService.checkFirstname(order, right, errors);

        assertTrue(errors.isEmpty());
        assertEquals(right, order.getFirstname());

        orderService.checkFirstname(order, invalid, errors);

        assertFalse(errors.isEmpty());
    }

    @Test
    public void testIsLastnameCorrect() {
        String right = "TestLastName";
        String invalid = "";
        Order order = new Order();
        Map<String, String> errors = new HashMap<>();

        orderService.checkLastname(order, right, errors);

        assertTrue(errors.isEmpty());
        assertEquals(right, order.getLastname());

        orderService.checkLastname(order, invalid, errors);

        assertFalse(errors.isEmpty());
    }

    @Test
    public void testIsDeliveryDateCorrect() {
        String right = LocalDate.now().toString();
        String empty = "";
        String inPast = LocalDate.now().minusDays(5).toString();
        Order order = new Order();
        Map<String, String> errors = new HashMap<>();

        orderService.checkDeliveryDate(order, right, errors);

        assertTrue(errors.isEmpty());
        assertEquals(LocalDate.parse(right), order.getDeliveryDate());

        orderService.checkDeliveryDate(order, empty, errors);

        assertFalse(errors.isEmpty());

        orderService.checkDeliveryDate(order, inPast, errors);

        assertFalse(errors.isEmpty());
    }

    @Test
    public void testIsPhoneCorrect() {
        String right = "+375111234567";
        String invalid = "+375111231251251";
        Order order = new Order();
        Map<String, String> errors = new HashMap<>();

        orderService.checkPhone(order, right, errors);

        assertTrue(errors.isEmpty());
        assertEquals(right, order.getPhone());

        orderService.checkPhone(order, invalid, errors);

        assertFalse(errors.isEmpty());
    }

    @Test
    public void testIsDeliveryAddressCorrect() {
        String right = "Some address";
        String invalid = "";
        Order order = new Order();
        Map<String, String> errors = new HashMap<>();

        orderService.checkDeliveryAddress(order, right, errors);

        assertTrue(errors.isEmpty());
        assertEquals(right, order.getDeliveryAddress());

        orderService.checkDeliveryAddress(order, invalid, errors);

        assertFalse(errors.isEmpty());
    }

    @Test
    public void testIsPaymentMethodCorrect() {
        String right = "Credit card";
        String invalid = "";
        Order order = new Order();
        Map<String, String> errors = new HashMap<>();

        orderService.checkPaymentMethod(order, right, errors);

        assertTrue(errors.isEmpty());
        assertEquals(PaymentMethod.getEnum(right), order.getPaymentMethod());

        orderService.checkPaymentMethod(order, invalid, errors);

        assertFalse(errors.isEmpty());
    }
}


