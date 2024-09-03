package com.wavemaker.leavemanagement.service;

public interface EmployeeService {
    String getEmployeeNameByEmployeeId(int employeeId);

    int getEmployeeIdByEmailId(String emailId);

    String getAccountInfo(String emailId);
}
