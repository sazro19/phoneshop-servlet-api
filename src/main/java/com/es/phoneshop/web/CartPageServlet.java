package com.es.phoneshop.web;

import com.es.phoneshop.model.product.cart.CartService;
import com.es.phoneshop.model.product.cart.DefaultCartService;
import com.es.phoneshop.model.product.cart.exception.OutOfStockException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class CartPageServlet extends HttpServlet {
    private CartService cartService;

    private static final String ERRORS_ATTRIBUTE = "errors";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("cart", cartService.getCart(request.getSession()));

        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");

        Map<Long, String> errors = new HashMap<>();
        for (int i = 0; i < productIds.length; i++) {
            Long productId = Long.valueOf(productIds[i]);

            int quantity;
            try {
                NumberFormat format = NumberFormat.getInstance(request.getLocale());
                quantity = format.parse(quantities[i]).intValue();
                cartService.update(cartService.getCart(request.getSession()), productId, quantity);
            } catch (ParseException | OutOfStockException e) {
                if (e.getClass().equals(ParseException.class)) {
                    errors.put(productId, "Not a number");
                } else {
                    errors.put(productId, "Out of stock, available " + ((OutOfStockException) e).getStockAvailable());
                }
            }
        }
        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?message=Cart updated");
        } else {
            request.setAttribute(ERRORS_ATTRIBUTE, errors);
            doGet(request, response);
        }
    }

    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }
}
