package com.wavemaker.leavemanagement.service.impl;

import com.wavemaker.leavemanagement.model.Leave;
import com.wavemaker.leavemanagement.repository.impl.EmployeeRepositoryImpl;
import com.wavemaker.leavemanagement.repository.impl.LeaveRepositoryImpl;
import com.wavemaker.leavemanagement.service.LeavesService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.time.LocalDate;

public class LeavesServiceImpl  implements LeavesService {
    private final Logger logger = LoggerFactory.getLogger(LeavesServiceImpl.class);
    private final EmployeeRepositoryImpl employeeRepository = new EmployeeRepositoryImpl();
    private final LeaveRepositoryImpl leaveRepository = new LeaveRepositoryImpl();

    public String getAllLeaves(String emailID) {
        logger.info("Email id in leave service {}", emailID);
        int employeeId = employeeRepository.getEmployeeIdByEmailId(emailID);
        return leaveRepository.getAllLeavesByEmployee(employeeId);
    }

    public String getAllLeaveRequests(String emailId) {
        int employeeId = employeeRepository.getEmployeeIdByEmailId(emailId);
        return leaveRepository.getLeavesRequests(employeeId);
    }


    public boolean requestLeave(String jsonData, String emailId) {
        int employeeId = employeeRepository.getEmployeeIdByEmailId(emailId);
        JSONObject jsonObject = new JSONObject(jsonData);
        jsonObject.put("employeeId", employeeId);
        employeeId = jsonObject.getInt("employeeId");

        LocalDate localDate = LocalDate.parse((CharSequence) jsonObject.get("fromDate"));
        Date formattedFromDate = Date.valueOf(localDate);

        LocalDate localDate2 = LocalDate.parse((CharSequence) jsonObject.get("toDate"));
        Date formattedToDate = Date.valueOf(localDate2);

        LocalDate localDate3 = LocalDate.parse((CharSequence) jsonObject.get("appliedOn"));
        Date formattedAppliedOnDate = Date.valueOf(localDate3);


        Leave newLeave = new Leave(
                jsonObject.getInt("employeeId"),
                jsonObject.getString("leaveType"),
                formattedFromDate,
                formattedToDate,
                formattedAppliedOnDate,
                jsonObject.getString("leaveDescription"),
                jsonObject.getString("statusOfLeave")
        );


        return leaveRepository.requestLeave(employeeId, newLeave);
    }

    @Override
    public String getMyLeavesSummary(String emailId) {
        int employeeId = employeeRepository.getEmployeeIdByEmailId(emailId);
        return leaveRepository.getMyLeavesSummary(employeeId);
    }
}
