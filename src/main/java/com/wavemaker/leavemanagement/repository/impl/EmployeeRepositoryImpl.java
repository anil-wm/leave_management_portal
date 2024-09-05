package com.wavemaker.leavemanagement.repository.impl;

import com.wavemaker.leavemanagement.config.DatabaseConnectionManager;
import com.wavemaker.leavemanagement.exception.ErrorResponse;
import com.wavemaker.leavemanagement.repository.EmployeeRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final Connection connection;

    private static Logger logger = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);

    private String getEmployeeNameQuery = "SELECT EMPLOYEE_NAME FROM EMPLOYEES WHERE EMPLOYEE_ID = ?";


    private String employeeIdQuery = "SELECT * FROM EMPLOYEES WHERE EMAIL_ID = ?";

    private String getAccountInfoQuery = "SELECT EMPLOYEE_NAME, EMPLOYEE_ID, GENDER, PHONE_NUMBER, MANAGER_ID " +
            "FROM EMPLOYEES WHERE EMAIL_ID = ?";

    private String getManagerName = "SELECT EMPLOYEE_NAME FROM EMPLOYEES WHERE EMPLOYEE_ID = ?";

    private String employeeGenderByEmployeeId = "SELECT GENDER FROM EMPLOYEES WHERE EMPLOYEE_ID = ?";

    public EmployeeRepositoryImpl() {
        try {
            this.connection = DatabaseConnectionManager.getInstance().getConnection();
        } catch (SQLException e) {
            logger.warn("Error in getting connection objet");
            throw new ErrorResponse("Error in getting connection objet " + e.getMessage());
        }
    }

    @Override
    public String getEmployeeNameById(int employeeId) {
        String employeeName = "";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getEmployeeNameQuery);
            preparedStatement.setInt(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            logger.info("employee name query {}", getEmployeeNameQuery);
            resultSet.next();
            logger.info("Retrieving employee name from employee table using employee id {} ", employeeId);
            employeeName = resultSet.getString("EMPLOYEE_NAME");
            logger.info("Employee name from employees table : {}", employeeName);

        } catch (SQLException e) {
            logger.error("Error fetching employee name with employee id {}", employeeId);
            logger.info("Error : {}", e.getMessage());
            throw new ErrorResponse(e.getMessage());

        }

        return employeeName;
    }


    @Override
    public int getEmployeeIdByEmailId(String emailId) {

        try {
            logger.info("Extracting employee with email {} existed in database ", emailId);

            PreparedStatement preparedStatement = connection.prepareStatement(employeeIdQuery);
            preparedStatement.setString(1, emailId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                logger.info("Employee ID {} exists in the database", resultSet.getInt("EMPLOYEE_ID"));
                return resultSet.getInt("EMPLOYEE_ID");
            } else {
                logger.info("No such employee exists with email ID {} ", emailId);
                return -1;
            }
        } catch (SQLException e) {
            logger.error("Error occurred while checking if email exists in the database with query ");
            logger.error("with query {} : {}", employeeIdQuery, e.getMessage());
            throw new ErrorResponse(e.getMessage());
        }
    }

    @Override
    public String getAccountDetails(String emailId) {
        JSONObject accountInfo = new JSONObject();

        try {
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            preparedStatement = connection.prepareStatement(getAccountInfoQuery);
            preparedStatement.setString(1, emailId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                accountInfo.put("employeeName", resultSet.getString("EMPLOYEE_NAME"));
                accountInfo.put("mobileNumber", resultSet.getString("PHONE_NUMBER"));
                accountInfo.put("emailId", emailId);
                accountInfo.put("employeeId", resultSet.getInt("EMPLOYEE_ID"));
                accountInfo.put("gender", resultSet.getString("GENDER"));
            }
            preparedStatement = connection.prepareStatement(getManagerName);
            preparedStatement.setInt(1, resultSet.getInt("MANAGER_ID"));
            resultSet = preparedStatement.executeQuery();
            resultSet.next();

            accountInfo.put("managerName", resultSet.getString("EMPLOYEE_NAME"));

        } catch (SQLException e) {
            logger.info("Exception occurred : {}", e.getMessage());
            throw new ErrorResponse("Error in getting connection objet");
        }

        logger.info("Account information returned is {} ", accountInfo);
        return accountInfo.toString();
    }

    @Override
    public String getEmployeeGenderByEmployeeId(int employeeId) {
        String gender = "";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(employeeGenderByEmployeeId);
            preparedStatement.setInt(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            gender = resultSet.getString("GENDER");
        } catch (SQLException e) {
            throw new ErrorResponse(e.getMessage());
        }
        return gender;
    }
}
