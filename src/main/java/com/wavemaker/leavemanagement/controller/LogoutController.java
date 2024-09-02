package com.wavemaker.leavemanagement.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(urlPatterns = "/logout")
public class LogoutController extends HttpServlet {

    private final static Logger logger = LoggerFactory.getLogger(LogoutController.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("In logout controller");

        HttpSession httpSession = request.getSession(false);
        response.setContentType("application/json");
        JSONObject jsonResponse = new JSONObject();

        if (httpSession != null) {
            logger.info("Invalidating existing Session");
            httpSession.invalidate();
            jsonResponse.put("message", "Successfully logged out");
            jsonResponse.put("status", "success");

            // Send the JSON response
            response.getWriter().write(jsonResponse.toString());
            logger.info("response on success: {}", jsonResponse);

            // Redirect the client after sending the JSON response
            response.setStatus(HttpServletResponse.SC_OK);
//            response.getWriter().flush();
//
            logger.info(" path : {}",request.getContextPath());
//            response.sendRedirect(request.getContextPath() + "/index.html");
        } else {
            jsonResponse.put("message", "Error logging out");
            jsonResponse.put("status", "error");

            // Send the JSON response
            response.getWriter().write(jsonResponse.toString());
            logger.info("response on failure : {}", jsonResponse);

            // Redirect the client after sending the JSON response
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().flush();
            response.sendRedirect(request.getContextPath() + "/login.html");
        }
    }
}

