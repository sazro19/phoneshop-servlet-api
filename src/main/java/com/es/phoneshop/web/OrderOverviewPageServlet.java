package com.es.phoneshop.web;

import com.es.phoneshop.model.product.order.ArrayListOrderDao;
import com.es.phoneshop.model.product.order.Order;
import com.es.phoneshop.model.product.order.OrderDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderOverviewPageServlet extends HttpServlet {
    private OrderDao orderDao;

    private static final String ORDER_ATTRIBUTE = "order";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String secureOrderId = request.getPathInfo().substring(1);
        Order order = orderDao.getItemBySecureOrderId(secureOrderId);
        request.setAttribute(ORDER_ATTRIBUTE, order);

        request.getRequestDispatcher("/WEB-INF/pages/orderOverview.jsp").forward(request, response);
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }
}
