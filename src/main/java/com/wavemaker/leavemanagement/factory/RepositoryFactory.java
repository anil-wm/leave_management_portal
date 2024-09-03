package com.wavemaker.leavemanagement.factory;

import com.wavemaker.leavemanagement.repository.EmployeeRepository;
import com.wavemaker.leavemanagement.repository.HolidayRepository;
import com.wavemaker.leavemanagement.repository.LeaveRepository;
import com.wavemaker.leavemanagement.repository.UserRepository;
import com.wavemaker.leavemanagement.repository.impl.EmployeeRepositoryImpl;
import com.wavemaker.leavemanagement.repository.impl.HolidayRepositoryImpl;
import com.wavemaker.leavemanagement.repository.impl.LeaveRepositoryImpl;
import com.wavemaker.leavemanagement.repository.impl.UserRepositoryImpl;

public class RepositoryFactory {


    private volatile EmployeeRepository employeeRepository;
    private volatile HolidayRepository holidayRepository;
    private volatile LeaveRepository leaveRepository;
    private volatile UserRepository userRepository;


    public EmployeeRepository getEmployeeRepository(String type) {
        if ("EmployeeRepository".equalsIgnoreCase(type)) {
            if (employeeRepository == null) {
                synchronized (this) {
                    if (employeeRepository == null) {
                        employeeRepository = new EmployeeRepositoryImpl();
                    }
                }
            }
        }
        return employeeRepository;
    }

    public HolidayRepository getHolidayRepository(String type) {
        if ("HolidayRepository".equalsIgnoreCase(type)) {
            if (holidayRepository == null) {
                synchronized (this) {
                    if (holidayRepository == null) {
                        holidayRepository = new HolidayRepositoryImpl();
                    }
                }
            }
        }
        return holidayRepository;
    }

    public LeaveRepository getLeaveRepository(String type) {
        if ("LeaveRepository".equalsIgnoreCase(type)) {
            if (leaveRepository == null) {
                synchronized (this) {
                    if (leaveRepository == null) {
                        leaveRepository = new LeaveRepositoryImpl();
                    }
                }
            }
        }
        return leaveRepository;
    }

    public UserRepository getUserRepository(String type) {
        if ("UserRepository".equalsIgnoreCase(type)) {
            if (userRepository == null) {
                synchronized (this) {
                    if (userRepository == null) {
                        userRepository = new UserRepositoryImpl();
                    }
                }
            }
        }
        return userRepository;
    }
}
