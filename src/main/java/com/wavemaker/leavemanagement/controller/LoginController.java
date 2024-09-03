package com.wavemaker.leavemanagement.controller;

import com.wavemaker.leavemanagement.service.impl.AuthenticationServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl();


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        logger.info("entered into login controller");

        JSONObject jsonResponse = new JSONObject();

        String emailId = request.getParameter("emailId");
        String password = request.getParameter("password");
        logger.info("email Id in controller {} {}", emailId, password);

        if (authenticationService.authenticateUser(emailId, password)) {
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute("user", httpSession.getId());
            httpSession.setAttribute("emailId", emailId);
            logger.info("Login success with given credentials");


            response.setContentType("application/json");
            jsonResponse.put("message", "Successfully logged in");
            jsonResponse.put("status", "success");

            logger.info("Path : {}", request.getContextPath());

            // Redirect
            response.sendRedirect(request.getContextPath() + "/homepage.html");
        } else {
            // If authentication fails, prepare the JSON response
            response.setContentType("application/json");
            String jsonResponseString = "{"
                    + "\"title\": \"You are a new user\","
                    + "\"heading\": \"Login Details\","
                    + "\"details\": {"
                    + "\"mailId\": \"" + emailId + "\""
                    + "}"
                    + "}";

            response.getWriter().write(jsonResponseString);
        }
    }
}


//  //   by using basic authentication
//        String authenticationHeader = request.getHeader("Authorization");
//        logger.info("getting authorization header {}",authenticationHeader);
//
//        if (authenticationHeader != null && authenticationHeader.startsWith("Basic")) {
//            String base64Credentials = authenticationHeader.substring("Basic".length()).trim();
//
//            String credentials = new String(
//                    Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8
//            );
//
//            final String[] values = credentials.split(":", 2);
//            logger.info("values  on login : {}", Arrays.toString(values));
//
//            if (values.length == 2) {
//                String emailId = values[0];
//                String password = values[1];
//
//                logger.info("email Id login controller {}",emailId);
//                logger.info("password login controller {}",password);
//
//                if (authenticationService.authenticateUser(emailId,password)){
//                    HttpSession httpSession = request.getSession();
//                    httpSession.setAttribute("userId", httpSession.getId());
//                    httpSession.setAttribute("emailId",emailId);
//                    response.addCookie(new Cookie("emailId", emailId));
//                    response.addCookie(new Cookie("password", password));
//                    response.getWriter().write("{\"message\": \"Login success..!!\"}");
//                    response.sendRedirect(request.getContextPath()+"/LeaveTracker_index.html");
//                    return ;
//                }
//                else{
//                    response.getWriter().print("{\"error\": \"Invalid credentials\"}");
//                    response.sendRedirect(request.getContextPath()+"/index.html");
//                    return ;
//                }
//            }
//        }
//        else if(authenticationHeader == null){
//            response.getWriter().print("{\"message\": \"Please provide your credential to access page\"}");
//            return ;
//        }
//
//        String emailId = request.getParameter("emailId");
//        String password = request.getParameter("password");
//        logger.info("email Id in controller {} {}",emailId,password);
//
//        if (authenticationService.authenticateUser(emailId,password)){
//            HttpSession httpSession = request.getSession();
//            httpSession.setAttribute("user", httpSession.getId());
//
//
//        }
//        else{
//            response.getWriter().print("{\"error\": \"Invalid credentials\"}");
//
//        }

