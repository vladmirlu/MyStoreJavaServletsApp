package com.selling.store.shop.basket;

import com.selling.store.shop.ItemsValidatorProvider;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Basket page servlet
 */
@SuppressWarnings("serial")
@WebServlet("/shop/basket")
public class BasketServlet extends HttpServlet {

    /**
     * BasketServlet logger.
     */
    private final Logger logger = Logger.getLogger(BasketServlet.class);

    /**
     * Service for providing validated items data
     */
    private final ItemsValidatorProvider provider = new ItemsValidatorProvider();

    /**
     * Forwards to the basket page
     *
     * @param request  http servlet request
     * @param response http servlet response
     * @throws ServletException when request dispatcher can not forward to basket page
     * @throws IOException      when request dispatcher executes the forward method
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.info("Basket page [/components/shop/basket.html] loaded with response status " + response.getStatus());
        request.getRequestDispatcher("/components/shop/basket.html").forward(request, response);
    }

    /**
     * Provides nonexistent vendor codes of items
     *
     * @param request  http servlet request
     * @param response http servlet response
     * @throws IOException when response output stream fails to write vendor codes
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String> codes = provider.getNonexistentItemCodes(Arrays.asList(request.getParameterValues("codes[]")));
        logger.info("Nonexistent items codes: " + codes + " Response status: " + response.getStatus());
        request.setAttribute("itemGaps", codes);
        response.getOutputStream().write(codes.toString().getBytes());
        response.getOutputStream().flush();
    }
}
