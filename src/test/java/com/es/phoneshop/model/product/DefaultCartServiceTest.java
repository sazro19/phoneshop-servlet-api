package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartService;
import com.es.phoneshop.model.product.cart.DefaultCartService;
import com.es.phoneshop.model.product.cart.exception.OutOfStockException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;

public class DefaultCartServiceTest {
    private ProductDao productDao;

    private CartService cartService;

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @After
    public void clearProductList() {
        productDao.findProducts(null, null, null)
                .forEach(p -> productDao.delete(p.getId()));
    }

    @Test
    public void testAddToCart() throws OutOfStockException {
        Cart cart = new Cart();
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        cartService.add(cart, product.getId(), 3);

        assertEquals(product, cart.getItems().get(0).getProduct());

        assertEquals(3, cart.getItems().get(0).getQuantity());
    }

    @Test
    public void testAddSeveralTimesOneProduct() throws OutOfStockException {
        Cart cart = new Cart();
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        cartService.add(cart, product.getId(), 3);
        cartService.add(cart, product.getId(), 4);

        assertEquals(product, cart.getItems().get(0).getProduct());

        assertEquals(7, cart.getItems().get(0).getQuantity());
    }

    @Test(expected = OutOfStockException.class)
    public void testOutOfStockException() throws OutOfStockException {
        Cart cart = new Cart();
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        cartService.add(cart, product.getId(), 200);
    }
}


