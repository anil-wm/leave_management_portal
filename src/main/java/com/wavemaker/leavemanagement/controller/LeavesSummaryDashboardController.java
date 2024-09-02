package com.wavemaker.leavemanagement.controller;

import com.wavemaker.leavemanagement.service.LeavesService;
import com.wavemaker.leavemanagement.service.impl.LeavesServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(urlPatterns = "/dashboard")
public class LeavesSummaryDashboardController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);

        String emailID = (String) httpSession.getAttribute("emailId");

        String leavesSummaryJson = new LeavesServiceImpl().getMyLeavesSummary(emailID);

        response.setContentType("application/json");
        response.getWriter().write(leavesSummaryJson);

    }
}
