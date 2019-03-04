package com.selling.store.shop;

import com.google.gson.Gson;
import com.selling.store.entity.Item;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Servlet for items retrieving
 */
@SuppressWarnings("serial")
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
     * Retrieves items and forwards them as html to the shop page
     *
     * @param request  http servlet request
     * @param response http servlet response
     * @throws IOException when response output stream fails to write items data
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Item> items = provider.getItems();
        logger.info("Received items: " + items + " Response status: " + response.getStatus());
        response.setContentType("text/html");
        String htmlString = "";
        for (Item item : items) {
            htmlString += "<tr>\n"
                    + "<td name='Name' scope='row'>" + item.getName() + "</td>\n"
                    + "<td name='Vendor code'>" + item.getCode() + "</td>\n"
                    + "<td name='Price'>" + item.getPrice() + "</td>\n"
                    + "<td name='Choose'><input type='checkbox' class='items-check'" +
                    " id='" + item.getCode()
                    + "' name='" + item.getName()
                    + "' value='" + item.getPrice() + "'/></td>\n"
                    + "</tr>\n";
        }
        PrintWriter out = response.getWriter();
        out.println(htmlString);
        out.flush();
        out.close();
    }
}
