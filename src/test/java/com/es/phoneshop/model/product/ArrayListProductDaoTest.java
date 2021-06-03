package com.es.phoneshop.model.product;

import com.es.phoneshop.data.DataGenerator;
import com.es.phoneshop.model.product.exception.ProductNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
        DataGenerator.generateSampleProducts().forEach(product -> {
            productDao.save(product);
        });
    }

    @After
    public void clearProductList() {
        productDao.findProducts(null, null, null)
                .forEach(p -> productDao.delete(p.getId()));
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

        Product update = new Product(product.getId(), product.getCode(), product.getDescription(), product.getPrice(), product.getCurrency(), product.getStock(), product.getImageUrl());
        update.setPrice(new BigDecimal(150));
        productDao.save(update);

        Product check = productDao.getProduct(update.getId());

        assertEquals(new BigDecimal(150), check.getPrice());
    }

    @Test
    public void testFindProductWithZeroStock() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        assertFalse(productDao.findProducts().contains(product));
    }

    @Test
    public void testFindProductWithNullPrice() throws ProductNotFoundException {
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

    @Test(expected = ProductNotFoundException.class)
    public void testDeleteNonExistentProduct() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("nonExistent", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        productDao.delete(product.getId());

        productDao.delete(product.getId());
    }

    @Test
    public void testGetProduct() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        assertEquals(product.getId(), productDao.getProduct(product.getId()).getId());
    }

    @Test(expected = ProductNotFoundException.class)
    public void testGetNonExistentProduct() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("nonExistent", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        productDao.delete(product.getId());

        productDao.getProduct(product.getId());
    }

    @Test
    public void testFindProductsWithQuery() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product p1 = new Product("p1", "Huawei", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product p2 = new Product("p2", "Huawei X", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product p3 = new Product("p3", "Huawei X 1", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(p3);
        productDao.save(p2);
        productDao.save(p1);

        String query = "Huawei X";

        List<Product> expected = Arrays.asList(productDao.getProduct(p2.getId()),
                productDao.getProduct(p3.getId()),
                productDao.getProduct(p1.getId()));

        assertEquals(expected, productDao.findProducts(query));
    }

    @Test
    public void testAscDescriptionSorting() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product p1 = new Product("p1", "Huawei", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product p2 = new Product("p2", "Huawei X", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product p3 = new Product("p3", "Huawei X 1", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(p1);
        productDao.save(p2);
        productDao.save(p3);

        String query = "Huawei X 1";

        List<Product> expected = Arrays.asList(productDao.getProduct(p1.getId()),
                productDao.getProduct(p2.getId()),
                productDao.getProduct(p3.getId()));

        assertEquals(expected, productDao.findProducts(query, SortField.DESCRIPTION, SortOrder.ASC));
    }

    @Test
    public void testDescDescriptionSorting() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product p1 = new Product("p1", "Huawei", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product p2 = new Product("p2", "Huawei X", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product p3 = new Product("p3", "Huawei X 1", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(p1);
        productDao.save(p2);
        productDao.save(p3);

        String query = "Huawei X 1";

        List<Product> expected = Arrays.asList(productDao.getProduct(p3.getId()),
                productDao.getProduct(p2.getId()),
                productDao.getProduct(p1.getId()));

        assertEquals(expected, productDao.findProducts(query, SortField.DESCRIPTION, SortOrder.DESC));
    }

    @Test
    public void testAscPriceSorting() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product p1 = new Product("p1", "Huawei", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product p2 = new Product("p2", "Huawei X", new BigDecimal(110), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product p3 = new Product("p3", "Huawei X 1", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(p1);
        productDao.save(p2);
        productDao.save(p3);

        String query = "Huawei X 1";

        List<Product> expected = Arrays.asList(productDao.getProduct(p1.getId()),
                productDao.getProduct(p2.getId()),
                productDao.getProduct(p3.getId()));

        assertEquals(expected, productDao.findProducts(query, SortField.PRICE, SortOrder.ASC));
    }

    @Test
    public void testDescPriceSorting() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product p1 = new Product("p1", "Huawei", new BigDecimal(300), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product p2 = new Product("p2", "Huawei X", new BigDecimal(200), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product p3 = new Product("p3", "Huawei X 1", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(p1);
        productDao.save(p2);
        productDao.save(p3);

        String query = "Huawei X 1";

        List<Product> expected = Arrays.asList(productDao.getProduct(p1.getId()),
                productDao.getProduct(p2.getId()),
                productDao.getProduct(p3.getId()));

        assertEquals(expected, productDao.findProducts(query, SortField.PRICE, SortOrder.DESC));
    }

    @Test
    public void testSaveNewPriceHistory() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product p = new Product("p1", "Huawei", new BigDecimal(300), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(p);

        Product updated = new Product(p.getId(),"p1", "Huawei", new BigDecimal(350), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(updated);

        assertEquals(new BigDecimal(350), productDao.getProduct(updated.getId()).getPriceHistoryList().get(0).getPrice());
        assertEquals(new BigDecimal(300), productDao.getProduct(updated.getId()).getPriceHistoryList().get(1).getPrice());
    }

    @Test
    public void testSaveUpdatedProductWithSamePrice() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product p = new Product("p1", "Huawei", new BigDecimal(300), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(p);

        Product updated = new Product(p.getId(),"p1", "Huawei", new BigDecimal(300), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(updated);

        assertEquals(1, productDao.getProduct(updated.getId()).getPriceHistoryList().size());
    }
}


