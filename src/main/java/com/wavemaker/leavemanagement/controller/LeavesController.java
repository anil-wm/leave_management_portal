package com.wavemaker.leavemanagement.controller;

import com.wavemaker.leavemanagement.factory.ServiceFactory;
import com.wavemaker.leavemanagement.service.LeavesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;


@WebServlet(urlPatterns = "/my_leaves_tracker")
public class LeavesController extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(LeavesController.class);
    private static ServiceFactory serviceFactory = null;
    private static LeavesService leavesService = null;

    @Override
    public void init() throws ServletException {
        serviceFactory = new ServiceFactory();
        leavesService = serviceFactory.getLeavesService("LeavesService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }
        String mailID = (String) httpSession.getAttribute("emailId");

        logger.info("user logged in with mail is {} ", mailID);

        String allLeaves = leavesService.getAllLeaves(mailID);

        response.setContentType("application/json");
        response.getWriter().write(allLeaves);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession httpSession = request.getSession(false);
        response.setContentType("application/json");
        if (httpSession == null) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }


        // Reading JSON data from the request body
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String jsonData = sb.toString();

        JSONObject jsonResponse = new JSONObject();

        logger.info("data received : {} ", jsonData);

        String mailID = (String) httpSession.getAttribute("emailId");
        boolean result = leavesService.requestLeave(jsonData, mailID);

        if (result) {
            jsonResponse.put("message", "Leave request sent successfully");
            jsonResponse.put("status", "success");
        } else {
            jsonResponse.put("message", "Error sending leave request");
            jsonResponse.put("status", "error");
        }

        response.getWriter().write(jsonResponse.toString());

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        String jsonString = stringBuilder.toString();

        JSONObject jsonObject = new JSONObject(jsonString);

        logger.info("leave id and new status received  : {} {}",
                jsonObject.getInt("leaveId"),
                jsonObject.getString("status"));

        String jsonResponse = leavesService.updateLeaveStatus(
                jsonObject.getInt("leaveId"),
                jsonObject.getString("status"));


        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);


    }
}
