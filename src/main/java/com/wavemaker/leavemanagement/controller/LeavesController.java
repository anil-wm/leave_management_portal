package com.wavemaker.leavemanagement.controller;

import com.wavemaker.leavemanagement.service.impl.LeavesServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;


@WebServlet(urlPatterns = "/my_leaves_tracker")
public class LeavesController extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(LeavesController.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
//            response.getWriter().println("Login First to perform operation on database");
            response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }
        String mailID = (String) httpSession.getAttribute("emailId");
        System.out.println(mailID);
        logger.info("user logged in with mail is {} ", mailID);
//        Cookie[] cookies =  request.getCookies();
        String allLeaves = new LeavesServiceImpl().getAllLeaves(mailID);

        response.setContentType("application/json");
        response.getWriter().write(allLeaves);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        HttpSession httpSession = request.getSession(false);
        response.setContentType("application/json");
        if (httpSession == null) {
          response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }


        // Read JSON data from the request body
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String jsonData = sb.toString();

//        // Save the user data to the database
        JSONObject jsonResponse = new JSONObject();

//        HttpSession httpSession = request.getSession(false);
        String mailID = (String) httpSession.getAttribute("emailId");
        boolean result = new LeavesServiceImpl().requestLeave(jsonData, mailID);

        if (result) {
            jsonResponse.put("message", "Leave request sent successfully");
            jsonResponse.put("status", "success");
        } else {
            jsonResponse.put("message", "Error sending leave request");
            jsonResponse.put("status", "error");
        }

        // Send JSON response back to the frontend
        response.getWriter().write(jsonResponse.toString());

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
