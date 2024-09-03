package com.wavemaker.leavemanagement.controller;

import com.wavemaker.leavemanagement.factory.ServiceFactory;
import com.wavemaker.leavemanagement.service.EmployeeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/my_account")
public class MyAccountController extends HttpServlet {

    private final static Logger logger = LoggerFactory.getLogger(MyAccountController.class);
    private static ServiceFactory serviceFactory = null;
    private static EmployeeService employeeService = null;

    @Override
    public void init() throws ServletException {
        serviceFactory = new ServiceFactory();
        employeeService = serviceFactory.getEmployeeService("EmployeeService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            response.sendRedirect(request.getContextPath() + "/homepage.html");
            return;
        }
        String emailId = (String) httpSession.getAttribute("emailId");
        logger.info("Email in Account Controller : {}", emailId);

        String myAccountJsonResponse = employeeService.getAccountInfo(emailId);
        logger.info("account info in account controller : {}", myAccountJsonResponse);
        response.setContentType("application/json");
        if (myAccountJsonResponse == null) {
            response.getWriter().write("{message} {No data found}");
            return;
        }
        response.getWriter().write(myAccountJsonResponse);
    }
}
