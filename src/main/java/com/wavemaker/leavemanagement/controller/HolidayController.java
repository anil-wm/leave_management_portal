package com.wavemaker.leavemanagement.controller;


import com.wavemaker.leavemanagement.factory.ServiceFactory;
import com.wavemaker.leavemanagement.service.HolidaysService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(urlPatterns = "/holidays")
public class HolidayController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(HolidayController.class);
    private static ServiceFactory serviceFactory;
    private static HolidaysService holidaysService;


    @Override
    public void init() throws ServletException {
        serviceFactory = new ServiceFactory();
        holidaysService = serviceFactory.getHolidaysService("HolidaysService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("In holiday Controller");
        String holidaysResponse = holidaysService.getAllHolidays();

        logger.info("Holidays : {}", holidaysResponse);

        logger.info("Extracted holidays are {}", holidaysResponse);
        response.setContentType("application/json");
        response.getWriter().write(holidaysResponse);

    }
}
