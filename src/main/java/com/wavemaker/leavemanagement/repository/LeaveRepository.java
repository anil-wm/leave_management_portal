package com.wavemaker.leavemanagement.repository;

import com.wavemaker.leavemanagement.model.Leave;

public interface LeaveRepository {

    String getAllLeavesByEmployee(int employeeId);

    String getLeavesRequests(int employeeId);

    boolean requestLeave(int employeeId, Leave newLeave);

    String getMyLeavesSummary(int employeeId);

    String leaveTakenByLeaveType(int employeeId, String leaveType);

    String updateLeaveStatus(int leaveId, String statusOfLeave);
}
