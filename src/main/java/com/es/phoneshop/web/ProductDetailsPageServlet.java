package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartService;
import com.es.phoneshop.model.product.cart.DefaultCartService;
import com.es.phoneshop.model.product.cart.exception.OutOfStockException;
import com.es.phoneshop.model.product.viewed.DefaultRecentlyViewedService;
import com.es.phoneshop.model.product.viewed.RecentlyViewedContainer;
import com.es.phoneshop.model.product.viewed.RecentlyViewedService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;

    private CartService cartService;

    private RecentlyViewedService recentlyViewedService;

    private static final String ERROR_ATTRIBUTE = "error";

    private static final String MESSAGE_ATTRIBUTE = "message";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        recentlyViewedService = DefaultRecentlyViewedService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = parseProductId(request);

        RecentlyViewedContainer container = recentlyViewedService.getContainer(request.getSession());

        request.setAttribute("product", productDao.getItem(productId));
        request.setAttribute("cart", cartService.getCart(request.getSession()));
        request.setAttribute("viewed", recentlyViewedService.getThreeRecentlyViewedProducts(container));

        recentlyViewedService.add(container, productId);

        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = parseProductId(request);
        String quantityString = request.getParameter("quantity");
        int quantity;
        try {
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(quantityString).intValue();
        } catch (ParseException e) {
            request.setAttribute(ERROR_ATTRIBUTE, "Not a number");
            doGet(request, response);
            return;
        }
        try {
            Cart cart = cartService.getCart(request.getSession());
            cartService.add(cart, productId, quantity);
        } catch (OutOfStockException | IllegalArgumentException e) {
            if (e.getClass().equals(OutOfStockException.class)) {
                request.setAttribute(ERROR_ATTRIBUTE, "Out of stock, available " +
                        ((OutOfStockException) e).getStockAvailable());
            } else {
                request.setAttribute(ERROR_ATTRIBUTE, e.getMessage());
            }
            doGet(request, response);
            return;
        }
        request.setAttribute(MESSAGE_ATTRIBUTE, "Product added to cart");

        response.sendRedirect(request.getContextPath() + "/products/" +
                productId + "?message=Product added to cart");
    }

    private Long parseProductId(HttpServletRequest request) {
        String productInfo = request.getPathInfo().substring(1);
        return Long.valueOf(productInfo);
    }

    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    public void setRecentlyViewedService(RecentlyViewedService recentlyViewedService) {
        this.recentlyViewedService = recentlyViewedService;
    }

    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }
}
