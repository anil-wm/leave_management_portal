package com.wavemaker.leavemanagement.repository.impl;

import com.wavemaker.leavemanagement.config.DatabaseConnectionManager;
import com.wavemaker.leavemanagement.factory.RepositoryFactory;
import com.wavemaker.leavemanagement.model.Leave;
import com.wavemaker.leavemanagement.repository.EmployeeRepository;
import com.wavemaker.leavemanagement.repository.LeaveRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;


public class LeaveRepositoryImpl implements LeaveRepository {

    private final Connection connection;
    private final static Logger logger = LoggerFactory.getLogger(LeaveRepositoryImpl.class);
    private EmployeeRepository employeeRepository = null;

    private final String getAllLeavesRequestQuery = "SELECT L.LEAVE_ID, L.EMPLOYEE_ID,E.EMPLOYEE_NAME,L.LEAVE_TYPE, " +
            " L.FROM_DATE, L.TO_DATE, L.APPLIED_ON, L.LEAVE_DESCRIPTION, L.STATUS_OF_LEAVE FROM LEAVES L" +
            " JOIN EMPLOYEES E" +
            " ON E.EMPLOYEE_ID = L.EMPLOYEE_ID" +
            " WHERE E.MANAGER_ID = ?" +
            " ORDER BY L.FROM_DATE;";

    private final String getAllLeavesQuery = "SELECT * FROM LEAVES WHERE EMPLOYEE_ID= ?";

    private final String requestLeaveQuery = "INSERT INTO LEAVES(" +
            "EMPLOYEE_ID,LEAVE_TYPE,FROM_DATE,TO_DATE,APPLIED_ON,LEAVE_DESCRIPTION,STATUS_OF_LEAVE) " +
            "VALUES(?,?,?,?,?,?,?)";

    private final String myLeavesSummaryQuery = "SELECT " +
            "    E.GENDER, " +
            "    E.EMPLOYEE_ID, " +
            "    E.EMPLOYEE_NAME, " +
            "    LT.LEAVE_TYPE, " +
            "    SUM(DATEDIFF(L.TO_DATE, L.FROM_DATE) + 1) AS TOTAL_DAYS_TAKEN, " +
            "    LT.ALLOWED_DAYS " +
            "  FROM " +
            "    LEAVES L " +
            " JOIN " +
            "    LEAVETYPES LT ON L.LEAVE_TYPE = LT.LEAVE_TYPE " +
            " JOIN " +
            "    EMPLOYEES E ON L.EMPLOYEE_ID = E.EMPLOYEE_ID " +
            " WHERE " +
            "    E.EMPLOYEE_ID = ? AND " +
            "    L.STATUS_OF_LEAVE = 'APPROVED'" +
            "    GROUP BY " +
            "    E.EMPLOYEE_ID, E.EMPLOYEE_NAME, LT.LEAVE_TYPE, LT.ALLOWED_DAYS " +
            " ORDER BY " +
            "    LT.LEAVE_TYPE;";

    private final String leavesTypesWithAllowedDays = "SELECT * FROM LEAVETYPES";

    String leavesTakenByLeaveTypeQuery = "SELECT" +
            "    employee_id," +
            "    leave_type," +
            "    SUM(DATEDIFF(to_date, from_date) + 1) AS total_leaves_taken" +
            " FROM" +
            "    leaves" +
            " WHERE" +
            "    employee_id = ?" +
            "    AND leave_type = ?" +
            " GROUP BY" +
            "    employee_id," +
            "    leave_type;";

    String allowedDaysForLeaveType = "SELECT ALLOWED_DAYS FROM LEAVETYPES WHERE LEAVE_TYPE = ? ";


    String updateLeaveStatusQuery = "UPDATE LEAVES SET STATUS_OF_LEAVE = ? WHERE LEAVE_ID = ?";


    public LeaveRepositoryImpl() {
        try {
            this.connection = DatabaseConnectionManager.getInstance().getConnection();
        } catch (SQLException e) {
            logger.warn("Error in getting connection objet");
            throw new RuntimeException(e);
        }
        employeeRepository = new RepositoryFactory().getEmployeeRepository("EmployeeRepository");
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
                jsonObject.put("employeeName", employeeRepository.getEmployeeNameById(
                        resultSet.getInt("EMPLOYEE_ID")));
                jsonObject.put("employeeId", resultSet.getInt("EMPLOYEE_ID"));
                jsonObject.put("leaveId", resultSet.getInt("LEAVE_ID"));
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
        } catch (Exception e) {
            logger.error("Error occurred while retrieving all leaves requests of manager from database ");
            logger.error("with the query {}: {}", getAllLeavesRequestQuery, e.getMessage());
        }
        logger.info("All my leaves summary {} ", jsonArray);
        return jsonArray.toString();
    }

    @Override
    public String getMyLeavesSummary(int employeeId) {

        JSONArray jsonArray = new JSONArray();
        Set<String> processedLeaveTypes = new HashSet<>(); // Track leave types to avoid duplicates

        try {
            // Query to get employee-specific leave data
            PreparedStatement preparedStatement = connection.prepareStatement(myLeavesSummaryQuery);
            preparedStatement.setInt(1, employeeId);

            logger.info("Retrieving my leave summary with query {} ", myLeavesSummaryQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            String gender = employeeRepository.getEmployeeGenderByEmployeeId(employeeId);

            while (resultSet.next()) {

                logger.info("In process of extracting leaves");
                String leaveType = resultSet.getString("LEAVE_TYPE");


                // Check if this leave type has already been processed
                if (!processedLeaveTypes.contains(leaveType)) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("employeeId", resultSet.getInt("EMPLOYEE_ID"));
                    jsonObject.put("leaveType", leaveType);
                    jsonObject.put("daysTaken", resultSet.getInt("TOTAL_DAYS_TAKEN"));
                    jsonObject.put("allowedDays", resultSet.getInt("ALLOWED_DAYS"));
                    jsonArray.put(jsonObject);
                    processedLeaveTypes.add(leaveType);
                }
            }

            // Query to get general leave types with allowed days
            Statement statement = connection.createStatement();
            ResultSet resultSet1 = statement.executeQuery(leavesTypesWithAllowedDays);

            while (resultSet1.next()) {
                String leaveType = resultSet1.getString("LEAVE_TYPE");

                // Check if this leave type has already been processed
                if (!processedLeaveTypes.contains(leaveType)) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("leaveType", leaveType);
                    jsonObject.put("allowedDays", resultSet1.getInt("ALLOWED_DAYS"));
                    jsonObject.put("daysTaken", 0);
                    jsonArray.put(jsonObject);
                    processedLeaveTypes.add(leaveType);
                }
            }

            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject jsonObject = jsonArray.getJSONObject(index);

                if (gender.equalsIgnoreCase("male") && jsonObject.getString("leaveType").equalsIgnoreCase("maternity leave")) {
                    jsonArray.remove(index);
                    break;
                }
                if (gender.equalsIgnoreCase("female") && jsonObject.getString("leaveType").equalsIgnoreCase("paternity leave")) {
                    jsonArray.remove(index);
                    break;
                }
            }


            resultSet.close();
            resultSet1.close();
            preparedStatement.close();
            statement.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        logger.info("Returned my leaves summary as : {}", jsonArray);
        return jsonArray.toString();
    }

    @Override
    public String leaveTakenByLeaveType(int employeeId, String leaveType) {

        int leavesTaken = 0;
        int allowedDays = 0;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(leavesTakenByLeaveTypeQuery);
            preparedStatement.setInt(1, employeeId);
            preparedStatement.setString(2, leaveType);

            resultSet = preparedStatement.executeQuery();

            resultSet.next();
            leavesTaken = resultSet.getInt("total_leaves_taken");

            preparedStatement = connection.prepareStatement(allowedDaysForLeaveType);
            preparedStatement.setString(1, leaveType);

            resultSet = preparedStatement.executeQuery();
            resultSet.next();

            allowedDays = resultSet.getInt("ALLOWED_DAYS");


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("leaveType", leaveType);
        jsonResponse.put("leavesTaken", leavesTaken);
        jsonResponse.put("allowedDays", allowedDays);

        logger.info("json response {}", jsonResponse);

        return jsonResponse.toString();
    }

    @Override
    public String updateLeaveStatus(int leaveId, String statusOfLeave) {
        int rowsAffected = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(updateLeaveStatusQuery);
            preparedStatement.setString(1, statusOfLeave);
            preparedStatement.setInt(2, leaveId);

            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        JSONObject jsonResponse = new JSONObject();
        if (rowsAffected > 0) {

            jsonResponse.put("status", "success");
            jsonResponse.put("message", "Status Updated successfully");
        } else {
            jsonResponse.put("status", "Failed");
            jsonResponse.put("message", "Status Updated Failed");
        }
        return jsonResponse.toString();
    }
}
