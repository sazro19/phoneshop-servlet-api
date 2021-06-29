package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.cart.exception.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertEquals;

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
    public void testAddThrowsOutOfStockException() throws OutOfStockException {
        Cart cart = new Cart();
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        cartService.add(cart, product.getId(), 200);
    }

    @Test
    public void testUpdateCart() throws OutOfStockException {
        Cart cart = new Cart();
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        cartService.add(cart, product.getId(), 5);

        cartService.update(cart, product.getId(), 10);

        assertEquals(10, cart.getItems().get(0).getQuantity());
    }

    @Test(expected = OutOfStockException.class)
    public void testUpdateThrowsOutOfStockException() throws OutOfStockException {
        Cart cart = new Cart();
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        cartService.update(cart, product.getId(), 200);
    }

    @Test
    public void testTotalQuantityAndPrice() throws OutOfStockException {
        Cart cart = new Cart();
        Currency usd = Currency.getInstance("USD");
        Product p1 = new Product("test1", "Samsung Galaxy S1", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product p2 = new Product("test2", "Samsung Galaxy S2", new BigDecimal(200), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(p1);
        productDao.save(p2);

        cartService.add(cart, p1.getId(), 5);
        cartService.add(cart, p2.getId(), 10);

        assertEquals(15, cart.getTotalQuantity());
        assertEquals(BigDecimal.valueOf(2500), cart.getTotalPrice());

        cartService.update(cart, p2.getId(), 5);

        assertEquals(10, cart.getTotalQuantity());
        assertEquals(BigDecimal.valueOf(1500), cart.getTotalPrice());
    }

    @Test
    public void testDeleteFromCart() throws OutOfStockException {
        Cart cart = new Cart();
        Currency usd = Currency.getInstance("USD");
        Product p1 = new Product("test1", "Samsung Galaxy S1", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product p2 = new Product("test2", "Samsung Galaxy S2", new BigDecimal(200), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(p1);
        productDao.save(p2);

        cartService.add(cart, p1.getId(), 5);
        cartService.add(cart, p2.getId(), 10);

        assertEquals(2, cart.getItems().size());

        cartService.delete(cart, p1.getId());

        assertEquals(1, cart.getItems().size());
    }
}


