package com.es.phoneshop.web;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.exception.OutOfStockException;
import com.es.phoneshop.model.viewed.RecentlyViewedContainer;
import com.es.phoneshop.model.viewed.RecentlyViewedService;
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
import java.util.Locale;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductListPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;
    @Mock
    private RecentlyViewedService service;
    @Mock
    private CartService cartService;

    private ProductListPageServlet servlet = new ProductListPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        servlet.setRecentlyViewedService(service);
        servlet.setCartService(cartService);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(service.getContainer(request.getSession())).thenReturn(new RecentlyViewedContainer());
        when(request.getPathInfo()).thenReturn("\\1");
        when(request.getLocale()).thenReturn(new Locale("en_US"));
        when(cartService.getCart(request.getSession())).thenReturn(new Cart());
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("products"), any());
        verify(request).setAttribute(eq("viewed"), any());
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"1"});
        when(request.getParameterValues("productId")).thenReturn(new String[]{"1"});
        when(request.getParameter("query")).thenReturn("Samsung");
        when(request.getParameter("sort")).thenReturn("PRICE");
        when(request.getParameter("order")).thenReturn("ASC");
        servlet.doPost(request, response);

        verify(response).sendRedirect(request.getContextPath() + "/products?message=Product added to cart" +
                "&query=Samsung" +
                "&sort=PRICE" +
                "&order=ASC");
    }

    @Test
    public void testDoPostWithParseException() throws ServletException, IOException {
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"e"});
        when(request.getParameterValues("productId")).thenReturn(new String[]{"1"});
        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), eq("Not a number"));
    }

    @Test
    public void testDoPostWithOutOfStockException() throws ServletException, IOException, OutOfStockException {
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"1"});
        when(request.getParameterValues("productId")).thenReturn(new String[]{"1"});
        doThrow(new OutOfStockException(new Product(), 1, 3)).when(cartService).add(any(), anyLong(), anyInt());
        servlet.doPost(request,response);

        verify(request).setAttribute(eq("error"), any());
    }
}
