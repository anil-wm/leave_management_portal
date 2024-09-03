package com.wavemaker.leavemanagement.service.impl;

import com.wavemaker.leavemanagement.factory.RepositoryFactory;
import com.wavemaker.leavemanagement.repository.EmployeeRepository;
import com.wavemaker.leavemanagement.service.EmployeeService;

public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository = null;


    public EmployeeServiceImpl() {
        employeeRepository = new RepositoryFactory().getEmployeeRepository("EmployeeRepository");
    }

    @Override
    public String getEmployeeNameByEmployeeId(int employeeId) {
        return employeeRepository.getEmployeeNameById(employeeId);
    }

    @Override
    public int getEmployeeIdByEmailId(String emailId) {
        return employeeRepository.getEmployeeIdByEmailId(emailId);
    }

    @Override
    public String getAccountInfo(String emailId) {
        return employeeRepository.getAccountDetails(emailId);
    }
}
