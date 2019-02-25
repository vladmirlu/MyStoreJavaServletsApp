package com.selling.store.shop;

import com.google.gson.Gson;
import com.selling.store.entity.Item;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Servlet for items retrieving
 */
@WebServlet("/mystore/shop/items")
public final class ItemsServlet extends HttpServlet {

    /**
     * ItemsServlet logger.
     */
    private final Logger logger = Logger.getLogger(ItemsServlet.class);

    /**
     * Service for providing validated items data
     */
    private ItemsValidatorProvider provider = new ItemsValidatorProvider();

    /**
     * Inits items data every 5 minutes
     */
    @Override
    public void init() {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> provider.initItems(), 0, 5, TimeUnit.MINUTES);
        logger.info("Items refreshed at: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    /**
     * Retrieves items and forwards them to the shop page
     *
     * @param request  http servlet request
     * @param response http servlet response
     * @throws IOException when response output stream fails to write items data
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Item> items = provider.getItems();
        logger.info("Received items: " + items + " Response status: " + response.getStatus());
        request.setAttribute("items", items);
        response.setContentType("application/json");
        response.getOutputStream().write(new Gson().toJson(items).getBytes());
        response.getOutputStream().flush();
    }
}