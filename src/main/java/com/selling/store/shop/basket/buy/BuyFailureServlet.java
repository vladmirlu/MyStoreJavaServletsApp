package com.selling.store.shop.basket.buy;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Buy failure servlet
 * */
@SuppressWarnings("serial")
@WebServlet("/shop/failure")
public class BuyFailureServlet extends HttpServlet {

    /**
     * Handles failure buy response
     *
     * @param request  http servlet request
     * @param response http servlet response
     * @throws ServletException  when request dispatcher can not forward to buy failure page
     * @throws IOException when request dispatcher executes the forward method
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger.getLogger(BuyFailureServlet.class).info("Buy failure!");
        request.getRequestDispatcher("/components/shop/failure.html").forward(request, response);
    }
}
