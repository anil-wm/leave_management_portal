package com.wavemaker.leavemanagement.service;

public interface LeavesService {
    String getAllLeaves(String emailID);

    String getAllLeaveRequests(String emailId);

    boolean requestLeave(String jsonData, String emailId);

    String getMyLeavesSummary(String emailId);

    String leaveTakenByLeaveType(int employeeId, String leaveType);

    String updateLeaveStatus(int leaveId, String updatedStatus);
}
