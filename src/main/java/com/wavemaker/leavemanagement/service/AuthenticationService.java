package com.wavemaker.leavemanagement.service;

public interface AuthenticationService {
    boolean authenticateUser(String emailId, String password);
}
