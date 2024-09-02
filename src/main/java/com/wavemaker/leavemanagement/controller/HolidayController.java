package com.wavemaker.leavemanagement.controller;


import com.wavemaker.leavemanagement.service.impl.HolidaysServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(urlPatterns = "/holidays")
public class HolidayController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(HolidayController.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws  IOException {

        String holidaysResponse = new HolidaysServiceImpl().getAllHolidays();
        logger.info("Holidays : {}", holidaysResponse);
        response.setContentType("application/json");
        response.getWriter().write(holidaysResponse);

    }
}
