package com.wavemaker.leavemanagement.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter(urlPatterns = {"/my_leaves_tracker", "/my_team_leave_requests", "/homepage.html",
        "/dashboard", "/my_account", "/holidays", "/leaveConfirmation"})
public class AuthenticationFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("In init method of authentication filter");
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        logger.info("Entered into doFilter method");

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        HttpSession httpSession = httpRequest.getSession(false);

        String loginPage = httpRequest.getContextPath() + "/index.html";
        String path = httpRequest.getRequestURI();

        logger.info("Login page path: {}", loginPage);
        logger.info("Request Path: {}", path);

        if (path.endsWith("/homepage.html") && httpSession == null) {
            httpResponse.sendRedirect(loginPage);
            return;
        }

        // Skip authentication
        if (path.endsWith("/logout.html") || path.endsWith("/logout")) {
            logger.info("Skipping authentication for public page");
            filterChain.doFilter(httpRequest, httpResponse);
            return;
        }


        // Checking if the user is logged in
        if (httpSession != null && httpSession.getAttribute("emailId") != null) {
            logger.info("User is logged in");
            filterChain.doFilter(httpRequest, httpResponse);
        } else {
            logger.info("User is not logged in, redirecting to login page");
            httpResponse.sendRedirect(loginPage);
        }
    }


    @Override
    public void destroy() {

    }
}
