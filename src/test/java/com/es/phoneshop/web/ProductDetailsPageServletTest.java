package com.es.phoneshop.web;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartService;
import com.es.phoneshop.model.product.cart.exception.OutOfStockException;
import com.es.phoneshop.model.product.viewed.RecentlyViewedContainer;
import com.es.phoneshop.model.product.viewed.RecentlyViewedService;
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
public class ProductDetailsPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;
    @Mock
    private ProductDao productDao;
    @Mock
    private CartService cartService;
    @Mock
    private RecentlyViewedService recentlyViewedService;

    private ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        servlet.setProductDao(productDao);
        servlet.setCartService(cartService);
        servlet.setRecentlyViewedService(recentlyViewedService);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getPathInfo()).thenReturn("\\1");
        when(request.getLocale()).thenReturn(new Locale("en_US"));
        when(productDao.getItem(any())).thenReturn(new Product());
        when(cartService.getCart(request.getSession())).thenReturn(new Cart());
        when(recentlyViewedService.getContainer(request.getSession())).thenReturn(new RecentlyViewedContainer());
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("\\1");
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("product"), any());
        verify(request).setAttribute(eq("viewed"), any());
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        when(request.getParameter(eq("quantity"))).thenReturn("3");
        servlet.doPost(request, response);

        verify(request).setAttribute(eq("message"), any());
        verify(response).sendRedirect(request.getContextPath() + "/products/1?message=Product added to cart");
    }

    @Test
    public void testDoPostWithParseException() throws ServletException, IOException {
        when(request.getParameter(eq("quantity"))).thenReturn("a");
        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), eq("Not a number"));
    }

    @Test
    public void testDoPostWithOutOfStockException() throws ServletException, IOException, OutOfStockException {
        when(request.getParameter(eq("quantity"))).thenReturn("1");
        doThrow(new OutOfStockException(new Product(), 1, 3)).when(cartService).add(any(), anyLong(), anyInt());
        servlet.doPost(request,response);

        verify(request).setAttribute(eq("error"), any());
    }
}
