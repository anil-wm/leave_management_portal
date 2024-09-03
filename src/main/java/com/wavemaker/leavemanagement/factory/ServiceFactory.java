package com.wavemaker.leavemanagement.factory;

import com.wavemaker.leavemanagement.service.EmployeeService;
import com.wavemaker.leavemanagement.service.HolidaysService;
import com.wavemaker.leavemanagement.service.LeavesService;
import com.wavemaker.leavemanagement.service.impl.EmployeeServiceImpl;
import com.wavemaker.leavemanagement.service.impl.HolidaysServiceImpl;
import com.wavemaker.leavemanagement.service.impl.LeavesServiceImpl;

public class ServiceFactory {


    private volatile EmployeeService employeeService;
    private volatile HolidaysService holidaysService;
    private volatile LeavesService leavesService;


    public EmployeeService getEmployeeService(String type) {
        if ("EmployeeService".equalsIgnoreCase(type)) {
            if (employeeService == null) {
                synchronized (this) {
                    if (employeeService == null) {
                        employeeService = new EmployeeServiceImpl();
                    }
                }
            }
        }
        return employeeService;
    }

    public HolidaysService getHolidaysService(String type) {
        if ("HolidaysService".equalsIgnoreCase(type)) {
            if (holidaysService == null) {
                synchronized (this) {
                    if (holidaysService == null) {
                        holidaysService = new HolidaysServiceImpl();
                    }
                }
            }
        }
        return holidaysService;
    }

    public LeavesService getLeavesService(String type) {
        if ("LeavesService".equalsIgnoreCase(type)) {
            if (leavesService == null) {
                synchronized (this) {
                    if (leavesService == null) {
                        leavesService = new LeavesServiceImpl();
                    }
                }
            }
        }
        return leavesService;
    }
}
