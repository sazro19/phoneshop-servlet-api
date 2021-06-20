package com.es.phoneshop.web;

import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartService;
import com.es.phoneshop.model.product.cart.DefaultCartService;
import com.es.phoneshop.model.product.order.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

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
            response.sendRedirect(request.getContextPath() + "/products");
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

        setOrderFieldOrErrors(request, OrderDetailsAttributes.FIRSTNAME.getValue(), errors, order::setFirstname);
        setOrderFieldOrErrors(request, OrderDetailsAttributes.LASTNAME.getValue(), errors, order::setLastname);

        String phone = request.getParameter(OrderDetailsAttributes.PHONE.getValue());
        if (phone == null || phone.isEmpty()) {
            errors.put(OrderDetailsAttributes.PHONE.getValue(), "phone is required");
        } else if (!orderService.isPhoneCorrect(phone)) {
            errors.put(OrderDetailsAttributes.PHONE.getValue(), "invalid phone number");
        } else {
            order.setPhone(phone);
        }

        String stringDate = request.getParameter(OrderDetailsAttributes.DELIVERY_DATE.getValue());
        if (stringDate == null || stringDate.isEmpty()) {
            errors.put(OrderDetailsAttributes.DELIVERY_DATE.getValue(), "delivery date is required");
        } else {
            LocalDate date = LocalDate.parse(stringDate);
            if (date.isBefore(LocalDate.now())) {
                errors.put(OrderDetailsAttributes.DELIVERY_DATE.getValue(), "invalid delivery date");
            } else {
                order.setDeliveryDate(date);
            }
        }

        setOrderFieldOrErrors(request, OrderDetailsAttributes.DELIVERY_ADDRESS.getValue(), errors, order::setDeliveryAddress);

        String paymentMethod = request.getParameter(OrderDetailsAttributes.PAYMENT_METHOD.getValue());
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            errors.put(OrderDetailsAttributes.PAYMENT_METHOD.getValue(), "payment method is required");
        } else {
            order.setPaymentMethod(PaymentMethod.getEnum(paymentMethod));
        }

        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            cartService.clearCart(cart);
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());
        } else {
            request.setAttribute(ERRORS_ATTRIBUTE, errors);
            request.setAttribute(ORDER_ATTRIBUTE, order);
            doGet(request, response);
        }
    }

    private void setOrderFieldOrErrors(HttpServletRequest request, String field, Map<String, String> errors,
                                       Consumer<String> consumer) {
        String attribute = request.getParameter(field);
        if (attribute == null || attribute.isEmpty()) {
            errors.put(field, field + " is required");
        } else {
            consumer.accept(attribute);
        }
    }

    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
