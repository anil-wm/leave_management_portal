package com.wavemaker.leavemanagement.repository.impl;

import com.wavemaker.leavemanagement.config.DatabaseConnectionManager;
import com.wavemaker.leavemanagement.model.Leave;
import com.wavemaker.leavemanagement.repository.LeaveRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LeaveRepositoryImpl implements LeaveRepository {

    private final Connection connection;
    private final static Logger logger = LoggerFactory.getLogger(LeaveRepositoryImpl.class);

    private final String getAllLeavesRequestQuery = "SELECT L.LEAVE_ID, L.EMPLOYEE_ID,E.EMPLOYEE_NAME,L.LEAVE_TYPE, " +
            " L.FROM_DATE, L.TO_DATE, L.APPLIED_ON, L.LEAVE_DESCRIPTION, L.STATUS_OF_LEAVE FROM LEAVES L" +
            " JOIN EMPLOYEES E" +
            " ON E.EMPLOYEE_ID = L.EMPLOYEE_ID" +
            " WHERE E.MANAGER_ID = ?"+
            " ORDER BY L.FROM_DATE;";

    private final String getAllLeavesQuery = "SELECT * FROM LEAVES WHERE EMPLOYEE_ID= ?";

    private final String requestLeaveQuery = "INSERT INTO LEAVES(" +
            "EMPLOYEE_ID,LEAVE_TYPE,FROM_DATE,TO_DATE,APPLIED_ON,LEAVE_DESCRIPTION,STATUS_OF_LEAVE) " +
            "VALUES(?,?,?,?,?,?,?)";

    private final String myLeavesSummaryQuery = "SELECT " +
            "    e.employee_id, " +
            "    e.employee_name, " +
            "    lt.leave_type, " +
            "    SUM(DATEDIFF(l.TO_DATE, l.FROM_DATE) + 1) AS total_days_taken, " +
            "    lt.allowed_days " +
            "FROM " +
            "    leaves l " +
            "JOIN " +
            "    leavetypes lt ON l.LEAVE_TYPE = lt.leave_type " +
            "JOIN " +
            "    employees e ON l.EMPLOYEE_ID = e.employee_id " +
            "WHERE " +
            "    e.employee_id = ? " +
            "    GROUP BY " +
            "    e.employee_id, e.employee_name, lt.leave_type, lt.allowed_days " +
            "ORDER BY " +
            "    lt.leave_type;";

    public LeaveRepositoryImpl() {
        try {
            this.connection = DatabaseConnectionManager.getInstance().getConnection();
        } catch (SQLException e) {
            logger.warn("Error in getting connection objet");
            throw new RuntimeException(e);
        }
    }


    @Override
    public String getAllLeavesByEmployee(int employeeId) {

        JSONArray jsonArray = new JSONArray();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getAllLeavesQuery);
            preparedStatement.setInt(1, employeeId);

            // Execute query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process result set
            while (resultSet.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("leaveId", resultSet.getInt("LEAVE_ID"));
                jsonObject.put("leaveType", resultSet.getString("LEAVE_TYPE"));
                jsonObject.put("fromDate", resultSet.getDate("FROM_DATE"));
                jsonObject.put("toDate", resultSet.getDate("TO_DATE"));
                jsonObject.put("appliedOn", resultSet.getDate("APPLIED_ON"));
                jsonObject.put("leaveDescription", resultSet.getString("LEAVE_DESCRIPTION"));
                jsonObject.put("statusOfLeave", resultSet.getString("STATUS_OF_LEAVE"));
                jsonArray.put(jsonObject);
            }

            logger.info("data sent as response {} ", jsonArray);
            // Close the connection
            resultSet.close();
            preparedStatement.close();
        } catch (Exception e) {
            logger.error("Error occurred while retrieving all leaves of an employee in database ");
            logger.error("with query {}: {}", getAllLeavesQuery, e.getMessage());

        }
        return jsonArray.toString();
    }

    @Override
    public boolean requestLeave(int employeeId, Leave newLeave) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requestLeaveQuery);
            preparedStatement.setInt(1, employeeId);
            preparedStatement.setString(2, newLeave.getLeaveType());
            preparedStatement.setDate(3, newLeave.getFromDate());
            preparedStatement.setDate(4, newLeave.getToDate());
            preparedStatement.setDate(5, newLeave.getAppliedOn());
            preparedStatement.setString(6, newLeave.getLeaveDescription());
            preparedStatement.setString(7, newLeave.getStatusOfLeave());

            int rows = preparedStatement.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            logger.error("error while requesting for a leave {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public String getLeavesRequests(int employeeId) {

        JSONArray jsonArray = new JSONArray();

        try {
            // SQL query
            PreparedStatement preparedStatement = connection.prepareStatement(getAllLeavesRequestQuery);
            preparedStatement.setInt(1, employeeId);

            logger.info("retrieving leave requests for a manager with query {} ", getAllLeavesRequestQuery);
            // Execute query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process result set
            while (resultSet.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("leaveType", resultSet.getString("LEAVE_TYPE"));
                jsonObject.put("totalDaysTaken", resultSet.getInt("TOTAL_DAYS_TAKEN"));
                jsonObject.put("allowedDays",resultSet.getInt("ALLOWED_TYPES"));

                jsonArray.put(jsonObject);
            }

            // Close the connection
            resultSet.close();
            preparedStatement.close();
        } catch (Exception e) {
            logger.error("Error occurred while retrieving all leaves requests of manager from database ");
            logger.error("with the query {}: {}", getAllLeavesRequestQuery, e.getMessage());
        }
        logger.info("All my leaves summary {} " ,jsonArray);
        return jsonArray.toString();
    }

    @Override
    public String getMyLeavesSummary(int employeeId) {

        JSONArray jsonArray = new JSONArray();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(myLeavesSummaryQuery);
            preparedStatement.setInt(1,employeeId);

            logger.info("Retrieving leave summary with query {} ", getAllLeavesRequestQuery);
            // Execute query
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("leaveId", resultSet.getInt("LEAVE_ID"));
                jsonObject.put("employeeId", resultSet.getInt("EMPLOYEE_ID"));


                // NEED TO MODIFY

                jsonObject.put("employeeName", new EmployeeRepositoryImpl().getEmployeeNameById(resultSet.getInt("EMPLOYEE_ID")));
                jsonObject.put("leaveType", resultSet.getString("LEAVE_TYPE"));
                jsonObject.put("fromDate", resultSet.getDate("FROM_DATE"));
                jsonObject.put("toDate", resultSet.getDate("TO_DATE"));
                jsonObject.put("appliedOn", resultSet.getDate("APPLIED_ON"));
                jsonObject.put("leaveDescription", resultSet.getString("LEAVE_DESCRIPTION"));
                jsonObject.put("statusOfLeave", resultSet.getString("STATUS_OF_LEAVE"));
                jsonArray.put(jsonObject);
            }

            // Close the connection
            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return "";
    }
}
