package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.exception.ProductNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindNotEmptyProductList() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testFindNullProducts() {
        assertTrue(productDao.findProducts().stream().noneMatch(Objects::isNull));
    }

    @Test
    public void testSaveNewProduct() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        assertTrue(product.getId() > 0);

        Product check = productDao.getProduct(product.getId());

        assertNotNull(check);
        assertEquals("sgs", check.getCode());
    }

    @Test
    public void testSaveUpdatedProduct() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        Product update = productDao.getProduct(product.getId());
        update.setPrice(new BigDecimal(150));
        productDao.save(update);

        Product check = productDao.getProduct(update.getId());

        assertEquals(new BigDecimal(150), check.getPrice());
    }

    @Test
    public void testFindProductWithZeroStock() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        assertFalse(productDao.findProducts().contains(product));
    }

    @Test
    public void testFindProductWithNullPrice() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("sgs", "Samsung Galaxy S", null, usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        assertFalse(productDao.findProducts().contains(product));
    }

    @Test
    public void testDeleteProduct() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        productDao.delete(product.getId());

        assertFalse(productDao.findProducts().contains(product));
    }

    @Test
    public void testDeleteNonExistentProduct() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("nonExistent", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        productDao.delete(product.getId());

        boolean check = false;
        try {
            productDao.delete(product.getId());
        } catch (ProductNotFoundException e) {
            check = true;
        }

        assertTrue(check);
    }

    @Test
    public void testGetProduct() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        assertEquals(product.getId(), productDao.getProduct(product.getId()).getId());
    }

    @Test
    public void testGetNonExistentProduct() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("nonExistent", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        productDao.delete(product.getId());

        boolean check = false;
        try {
            productDao.getProduct(product.getId());
        } catch (ProductNotFoundException e) {
            check = true;
        }

        assertTrue(check);
    }
}
