package com.es.phoneshop.web;

import com.es.phoneshop.data.DataGenerator;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DemoDataServletContextListener implements ServletContextListener {
    private ProductDao productDao;

    public DemoDataServletContextListener() {
        this.productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        if (Boolean.parseBoolean(servletContextEvent.getServletContext().getInitParameter("insertDemoData"))) {
            DataGenerator.generateSampleProducts().forEach(product -> {
                productDao.save(product);
            });
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
