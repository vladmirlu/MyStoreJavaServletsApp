package com.selling.store.shop.basket.buy;

import com.selling.store.shop.ItemsValidatorProvider;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Object for managing and mapping the pages related to the buy process
 */
@SuppressWarnings("serial")
@WebServlet(name = "BuyServlet",
        description = "Buy pages servlet",
        urlPatterns = {"/mystore/buyService", "/mystore/shop/success"})
public class BuyServlet extends HttpServlet {

    /**
     * BuyServlet logger.
     */
    private final Logger logger = Logger.getLogger(BuyServlet.class);

    /**
     * Object for providing validated data
     */
    private final ItemsValidatorProvider provider = new ItemsValidatorProvider();

    /**
     * Service for saving the buy related data
     */
    private final ItemsFileWriter itemsFileWriter = new ItemsFileWriter();

    /**
     * Manage buy items saving
     *
     * @param request  http servlet request
     * @param response http servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Buy items with codes: " + request.getParameterValues("codes[]"));
        itemsFileWriter.writeItemsAsCSV(provider.extractExistingItems(request, response), response);
    }

    /**
     * Handles success buy response
     *
     * @param request  http servlet request
     * @param response http servlet response
     * @throws ServletException when request dispatcher can not forward to buy success page
     * @throws IOException      when request dispatcher executes the forward method
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Buy success. Response status: " + response.getStatus());
        request.getRequestDispatcher("/components/shop/success.html").forward(request, response);
    }
}
