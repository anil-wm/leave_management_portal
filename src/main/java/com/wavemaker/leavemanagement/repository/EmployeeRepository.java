package com.wavemaker.leavemanagement.repository;

public interface EmployeeRepository {
    String getEmployeeNameById(int employeeId);

    int getEmployeeIdByEmailId(String emailId);

    String getAccountDetails(String emailId);

    String getEmployeeGenderByEmployeeId(int employeeId);
}
