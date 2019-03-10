package com.selling.store.shop;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Shop page servlet
 * */
@SuppressWarnings("serial")
@WebServlet("/shop")
public final class ShopServlet extends HttpServlet {

    /**
     * Forwards to the application shop page
     *
     * @param request  http servlet request
     * @param response http servlet response
     * @throws ServletException  when request dispatcher can not forward to shop page
     * @throws IOException       when request dispatcher executes the forward method
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Logger.getLogger(ShopServlet.class).info("Shop page [/components/shop/shop.html] loaded with response status " + response.getStatus());
        request.getRequestDispatcher("/components/shop/shop.html").forward(request, response);
    }

}