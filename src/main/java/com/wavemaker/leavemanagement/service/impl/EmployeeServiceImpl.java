package com.wavemaker.leavemanagement.service.impl;

import com.wavemaker.leavemanagement.repository.impl.EmployeeRepositoryImpl;
import com.wavemaker.leavemanagement.service.EmployeeService;

public class EmployeeServiceImpl  implements EmployeeService {


    @Override
    public String getEmployeeNameByEmployeeId(int employeeId) {
        return new EmployeeRepositoryImpl().getEmployeeNameById(employeeId);
    }

    @Override
    public int getEmployeeIdByEmailId(String emailId) {
        return new EmployeeRepositoryImpl().getEmployeeIdByEmailId(emailId);
    }

    @Override
    public String getAccountInfo(String emailId) {
        return new EmployeeRepositoryImpl().getAccountDetails(emailId);
    }
}
