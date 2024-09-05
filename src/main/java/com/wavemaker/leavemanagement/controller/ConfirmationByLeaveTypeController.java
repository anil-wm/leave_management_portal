package com.wavemaker.leavemanagement.controller;

import com.wavemaker.leavemanagement.factory.ServiceFactory;
import com.wavemaker.leavemanagement.service.LeavesService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(urlPatterns = "/leaveConfirmation")
public class ConfirmationByLeaveTypeController extends HttpServlet {

    private static ServiceFactory serviceFactory;
    private static LeavesService leavesService;


    @Override
    public void init() {
        serviceFactory = new ServiceFactory();
        leavesService = serviceFactory.getLeavesService("LeavesService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String leaveType = request.getParameter("leaveType");
        int employeeId = -1;
        String result = null;
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            response.sendRedirect(request.getContextPath() + "/homepage.html");
            return;
        }
        employeeId = Integer.parseInt(request.getParameter("employeeId"));
        result = leavesService.leaveTakenByLeaveType(employeeId, leaveType);

        response.setContentType("application/json");
        response.getWriter().write(result);

    }
}
