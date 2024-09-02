package com.wavemaker.leavemanagement.service.impl;


import com.wavemaker.leavemanagement.repository.impl.UserRepositoryImpl;
import com.wavemaker.leavemanagement.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepositoryImpl userRepositoryImpl = new UserRepositoryImpl();
    private final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    public boolean authenticateUser(String emailId, String password) {
        boolean userExists = userRepositoryImpl.getUserByEmailId(emailId);
        logger.info("user exists : {} ", userExists);
        logger.info("password match : {}", userRepositoryImpl.getPasswordByEmailId(emailId).equals(password));
        return userExists && userRepositoryImpl.getPasswordByEmailId(emailId).equalsIgnoreCase(password);
    }

}
