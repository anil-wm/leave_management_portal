package com.wavemaker.leavemanagement.service;

public interface LeavesService {
    String getAllLeaves(String emailID);
    String getAllLeaveRequests(String emailId);
    boolean requestLeave(String jsonData, String emailId);
    String getMyLeavesSummary(String emailId);
}
