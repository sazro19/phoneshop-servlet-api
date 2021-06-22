package com.es.phoneshop.web;

import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartService;
import com.es.phoneshop.model.product.order.Order;
import com.es.phoneshop.model.product.order.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;
    @Mock
    private CartService cartService;
    @Mock
    private OrderService orderService;

    private CheckoutPageServlet servlet = new CheckoutPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        servlet.setCartService(cartService);
        servlet.setOrderService(orderService);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(cartService.getCart(request.getSession())).thenReturn(new Cart());
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(orderService.getOrder(cartService.getCart(request.getSession()))).thenReturn(new Order());
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("order"), any());
        verify(request).setAttribute(eq("paymentMethods"), any());
    }

    @Test
    public void testDoGetWithNullOrder() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(response).sendRedirect(request.getContextPath() + "/products");
    }

    @Test
    public void testDoPostWithErrors() throws ServletException, IOException {
        when(orderService.getOrder(cartService.getCart(request.getSession()))).thenReturn(new Order());
        servlet.doPost(request, response);

        verify(request, atLeast(2)).setAttribute(eq("order"), any());
        verify(request).setAttribute(eq("errors"), any());
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        when(request.getParameter("firstname")).thenReturn("a");
        when(request.getParameter("lastname")).thenReturn("b");
        when(request.getParameter("phone")).thenReturn("+375111234567");
        when(request.getParameter("deliveryAddress")).thenReturn("c");
        when(request.getParameter("deliveryDate")).thenReturn(String.valueOf(LocalDate.now().plusDays(1)));
        when(request.getParameter("paymentMethod")).thenReturn("Cache");
        when(orderService.getOrder(cartService.getCart(request.getSession()))).thenReturn(new Order());
        when(orderService.isPhoneCorrect(anyString())).thenReturn(true);
        servlet.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }
}
