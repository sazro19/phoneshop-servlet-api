package com.es.phoneshop.web.filter;

import com.es.phoneshop.model.viewed.DefaultRecentlyViewedService;
import com.es.phoneshop.model.viewed.RecentlyViewedContainer;
import com.es.phoneshop.model.viewed.RecentlyViewedService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TheSamePageFilter implements Filter {
    private RecentlyViewedService service;

    private final static String PDP_PAGE_URL = "/products/";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        service = DefaultRecentlyViewedService.getInstance();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        RecentlyViewedContainer container = service.getContainer(((HttpServletRequest) request).getSession());
        if (!((HttpServletRequest) request).getRequestURI().contains(PDP_PAGE_URL)) {
            service.setLastPage(container, ((HttpServletRequest) request).getRequestURI());
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
