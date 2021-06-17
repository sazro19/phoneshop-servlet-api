package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;
import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartService;
import com.es.phoneshop.model.product.cart.DefaultCartService;
import com.es.phoneshop.model.product.cart.exception.OutOfStockException;
import com.es.phoneshop.model.product.viewed.DefaultRecentlyViewedService;
import com.es.phoneshop.model.product.viewed.RecentlyViewedService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public class ProductListPageServlet extends HttpServlet {
    private ProductDao productDao;

    private CartService cartService;

    private RecentlyViewedService recentlyViewedService;

    private static final String ERROR_ATTRIBUTE = "error";

    private static final String ERROR_INDEX = "index";

    private static final String ERROR_INPUT = "input";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        recentlyViewedService = DefaultRecentlyViewedService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");
        request.setAttribute("products", productDao.findProducts(query,
                sortField == null || sortField.equals("") ? null : SortField.valueOf(sortField),
                sortOrder == null || sortOrder.equals("") ? null : SortOrder.valueOf(sortOrder)));
        request.setAttribute("viewed", recentlyViewedService
                .getThreeRecentlyViewedProducts(recentlyViewedService.getContainer(request.getSession())));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = parseProductId(request);
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");

        String quantityString = "";
        for (int i = 0; i < productIds.length; i++) {
            if (productId.equals(Long.valueOf(productIds[i]))) {
                quantityString = quantities[i];
                break;
            }
        }

        int quantity;
        try {
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(quantityString).intValue();
        } catch (ParseException e) {
            request.setAttribute(ERROR_ATTRIBUTE, "Not a number");
            request.setAttribute(ERROR_INDEX, productId);
            request.setAttribute(ERROR_INPUT, quantityString);

            doGet(request, response);
            return;
        }
        try {
            Cart cart = cartService.getCart(request.getSession());
            cartService.add(cart, productId, quantity);
        } catch (OutOfStockException | IllegalArgumentException e) {
            request.setAttribute(ERROR_INDEX, productId);
            request.setAttribute(ERROR_INPUT, quantityString);
            if (e.getClass().equals(OutOfStockException.class)) {
                request.setAttribute(ERROR_ATTRIBUTE, "Out of stock, available " + ((OutOfStockException) e).getStockAvailable());
            } else {
                request.setAttribute(ERROR_ATTRIBUTE,  e.getMessage());
            }
            doGet(request, response);
            return;
        }
        String query = request.getParameter("query");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");
        response.sendRedirect(request.getContextPath() + "/products?message=Product added to cart" +
                "&query=" + query +
                "&sort=" + sortField +
                "&order=" + sortOrder);
    }

    private Long parseProductId(HttpServletRequest request) {
        String productInfo = request.getPathInfo().substring(1);
        return Long.valueOf(productInfo);
    }

    public void setRecentlyViewedService(RecentlyViewedService recentlyViewedService) {
        this.recentlyViewedService = recentlyViewedService;
    }

    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }
}
