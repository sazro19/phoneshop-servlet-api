package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.viewed.DefaultRecentlyViewedService;
import com.es.phoneshop.model.product.viewed.RecentlyViewedContainer;
import com.es.phoneshop.model.product.viewed.RecentlyViewedService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

public class DefaultRecentlyViewedServiceTest {
    private ProductDao productDao;

    private RecentlyViewedService service;

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
        service = DefaultRecentlyViewedService.getInstance();
    }

    @After
    public void clearProductList() {
        productDao.findProducts(null, null, null)
                .forEach(p -> productDao.delete(p.getId()));
    }

    @Test
    public void testAddToRecentlyViewedContainer() {
        Currency usd = Currency.getInstance("USD");
        Product p1 = new Product("p1", "Huawei", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product p2 = new Product("p2", "Huawei X", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product p3 = new Product("p3", "Huawei X 1", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(p3);
        productDao.save(p2);
        productDao.save(p1);
        RecentlyViewedContainer container = new RecentlyViewedContainer();

        service.add(container, p1.getId());
        service.add(container, p2.getId());
        service.add(container, p3.getId());

        long i = 1L;
        for (Product product : container.getThreeLastProducts()) {
            assertEquals(product, productDao.getProduct(i++));
        }
    }

    @Test
    public void testAddContainedProductToRecentlyViewedContainer() {
        Currency usd = Currency.getInstance("USD");
        Product p1 = new Product("p1", "Huawei", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product p2 = new Product("p2", "Huawei X", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product p3 = new Product("p3", "Huawei X 1", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(p2);
        productDao.save(p3);
        productDao.save(p1);
        RecentlyViewedContainer container = new RecentlyViewedContainer();

        service.add(container, p1.getId());
        service.add(container, p2.getId());
        service.add(container, p3.getId());
        service.add(container, p2.getId());

        long i = 1L;
        for (Product product : container.getThreeLastProducts()) {
            assertEquals(product, productDao.getProduct(i++));
        }
    }
}


