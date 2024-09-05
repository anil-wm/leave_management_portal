package com.wavemaker.leavemanagement.repository.impl;


import com.wavemaker.leavemanagement.config.DatabaseConnectionManager;
import com.wavemaker.leavemanagement.exception.ErrorResponse;
import com.wavemaker.leavemanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepositoryImpl implements UserRepository {

    private static Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    private final Connection connection;
    private String getPasswordWithEmailQuery = "SELECT PASSWORD FROM LOGIN_CREDENTIALS WHERE EMAIL_ID = ?";
    private String emailExistsQuery = "SELECT 1 FROM LOGIN_CREDENTIALS WHERE EMAIL_ID = ?";


    public UserRepositoryImpl() {
        try {
            this.connection = DatabaseConnectionManager.getInstance().getConnection();
        } catch (SQLException e) {
            logger.warn("Error in getting connection objet");
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean getUserByEmailId(String emailId) {


        try {
            logger.info("Checking if email exists in database: {}", emailId);
            PreparedStatement preparedStatement = connection.prepareStatement(emailExistsQuery);
            preparedStatement.setString(1, emailId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                logger.info("Email ID {} exists in the database", emailId);
                return true;
            } else {

                logger.info("No such email ID {} exists in the database", emailId);
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error occurred while checking if email exists in the database ");
            logger.error("with query {}: {}", emailExistsQuery, e.getMessage());
            throw new ErrorResponse(e.getMessage());
        }
    }

    @Override
    public String getPasswordByEmailId(String emailId) {

        String password = "";

        try (PreparedStatement preparedStatement = connection.prepareStatement(getPasswordWithEmailQuery)) {
            preparedStatement.setString(1, emailId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    password = resultSet.getString("PASSWORD");
                    logger.info("password from db : {}", password);
                } else {
                    logger.warn("No password found for email ID: {}", emailId);
                }
            }
        } catch (SQLException e) {
            logger.error("Error occurred while retrieving password for email ID {}: {}", emailId, e.getMessage());
            throw new ErrorResponse(e.getMessage());
        }

        return password;
    }
}
