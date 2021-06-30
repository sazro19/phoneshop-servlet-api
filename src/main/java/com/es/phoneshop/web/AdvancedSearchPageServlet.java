package com.es.phoneshop.web;

import com.es.phoneshop.model.advancedSearch.AdvancedSearchService;
import com.es.phoneshop.model.advancedSearch.DefaultAdvancedSearchService;
import com.es.phoneshop.model.advancedSearch.DescriptionOptions;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class AdvancedSearchPageServlet extends HttpServlet {
    private static final String ERROR_ATTRIBUTE = "errors";
    private static final String DESCRIPTION_OPTIONS_ATTRIBUTE = "options";

    private AdvancedSearchService advancedSearchService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        advancedSearchService = DefaultAdvancedSearchService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String description = request.getParameter("description");
        String stringMinPrice = request.getParameter("minPrice");
        String stringMaxPrice = request.getParameter("maxPrice");
        BigDecimal minPrice = null;
        BigDecimal maxPrice = null;

        Map<String, String> errors = new HashMap<>();
        if (stringMinPrice != null && stringMaxPrice != null){
            if (!stringMinPrice.isEmpty() || !stringMaxPrice.isEmpty()) {
                NumberFormat format = NumberFormat.getInstance(request.getLocale());
                try {
                    minPrice = BigDecimal.valueOf(format.parse(stringMinPrice).doubleValue());
                } catch (ParseException e) {
                    errors.put("minPrice", stringMinPrice);
                }
                try {
                    maxPrice = BigDecimal.valueOf(format.parse(stringMaxPrice).doubleValue());
                } catch (ParseException e) {
                    errors.put("maxPrice", stringMaxPrice);
                }
            }
        }
        String stringOption = request.getParameter("descriptionOptions");
        DescriptionOptions option = null;
        if (stringOption != null) {
            option = DescriptionOptions.getEnum(stringOption);
        }

        request.setAttribute(ERROR_ATTRIBUTE, errors);
        request.setAttribute("products", advancedSearchService.findProducts(description, minPrice, maxPrice, option));
        request.setAttribute(DESCRIPTION_OPTIONS_ATTRIBUTE, DescriptionOptions.getStringOptions());

        request.getRequestDispatcher("/WEB-INF/pages/advancedSearch.jsp").forward(request, response);
    }
}
