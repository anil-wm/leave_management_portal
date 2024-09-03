package com.wavemaker.leavemanagement.controller;

import com.wavemaker.leavemanagement.factory.ServiceFactory;
import com.wavemaker.leavemanagement.service.LeavesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(urlPatterns = "/my_team_leave_requests")
public class TeamLeavesController extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(TeamLeavesController.class);
    private static LeavesService leavesService = null;

    @Override
    public void init() throws ServletException {
        leavesService = new ServiceFactory().getLeavesService("LeavesService");
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }
        String mailID = (String) httpSession.getAttribute("emailId");
        logger.info("user logged in with email id is {} ", mailID);

        String allLeaves = leavesService.getAllLeaveRequests(mailID);

        response.setContentType("application/json");
        response.getWriter().write(allLeaves);
    }


}
