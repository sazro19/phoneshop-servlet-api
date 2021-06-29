package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.order.DefaultOrderService;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CheckoutPageServlet extends HttpServlet {
    private CartService cartService;

    private OrderService orderService;

    private static final String ERRORS_ATTRIBUTE = "errors";

    private static final String ORDER_ATTRIBUTE = "order";

    private static final String PAYMENT_METHODS_ATTRIBUTE = "paymentMethods";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request.getSession());
        Order order = orderService.getOrder(cart);
        if (order == null) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        request.setAttribute(ORDER_ATTRIBUTE, orderService.getOrder(cart));
        request.setAttribute(PAYMENT_METHODS_ATTRIBUTE, orderService.getStringPaymentMethods());

        request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        Cart cart = cartService.getCart(request.getSession());
        Order order = orderService.getOrder(cart);

        Map<String, String> errors = new HashMap<>();

        String firstname = request.getParameter(OrderDetailsAttributes.getFIRSTNAME());
        orderService.setOrderFieldOrErrors(firstname, OrderDetailsAttributes.getFIRSTNAME(), errors, order::setFirstname);

        String lastname = request.getParameter(OrderDetailsAttributes.getLASTNAME());
        orderService.setOrderFieldOrErrors(lastname, OrderDetailsAttributes.getLASTNAME(), errors, order::setLastname);

        String deliveryAddress = request.getParameter(OrderDetailsAttributes.getDeliveryAddress());
        orderService.setOrderFieldOrErrors(deliveryAddress, OrderDetailsAttributes.getDeliveryAddress(), errors, order::setDeliveryAddress);

        orderService.checkPhone(order, request.getParameter(OrderDetailsAttributes.getPHONE()), errors);

        orderService.checkDeliveryDate(order, request.getParameter(OrderDetailsAttributes.getDeliveryDate()), errors);

        orderService.checkPaymentMethod(order, request.getParameter(OrderDetailsAttributes.getPaymentMethod()), errors);

        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            cartService.clearCart(cart);
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());
        } else {
            request.setAttribute(ERRORS_ATTRIBUTE, errors);
            request.setAttribute(ORDER_ATTRIBUTE, order);
            request.setAttribute(PAYMENT_METHODS_ATTRIBUTE, orderService.getStringPaymentMethods());
            request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
        }
    }

    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
