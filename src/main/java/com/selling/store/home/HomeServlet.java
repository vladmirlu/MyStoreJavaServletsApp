package com.selling.store.home;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Home page servlet
 */
@SuppressWarnings("serial")
@WebServlet("")
public final class HomeServlet extends HttpServlet {

    /**
     * Configures the application logging properties with log4j.properties file
     */
    @Override
    public void init() {
        PropertyConfigurator.configure("log4j.properties");
    }

    /**
     * Forwards to the application home page
     *
     * @param request  http servlet request
     * @param response http servlet response
     * @throws ServletException  when request dispatcher can not forward to the home page
     * @throws IOException       when request dispatcher executes the forward method
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Logger.getLogger(HomeServlet.class).info("Home page [/components/home/home.html] loaded with response status " + response.getStatus());
        request.getRequestDispatcher("/components/home/home.html").forward(request, response);
    }
}
