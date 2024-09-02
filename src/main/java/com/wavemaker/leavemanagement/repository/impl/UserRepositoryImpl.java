package com.wavemaker.leavemanagement.repository.impl;


import com.wavemaker.leavemanagement.config.DatabaseConnectionManager;
import com.wavemaker.leavemanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

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

            // Use PreparedStatement to prevent SQL injection
            PreparedStatement preparedStatement = connection.prepareStatement(emailExistsQuery);
            preparedStatement.setString(1, emailId);

            // Execute the query and process the ResultSet
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // If a result is found, email exists
                logger.info("Email ID {} exists in the database", emailId);
                return true;
            } else {
                // No result found, email does not exist
                logger.info("No such email ID {} exists in the database", emailId);
                return false;
            }
        } catch (SQLException e) {
            // Log and handle the exception
            logger.error("Error occurred while checking if email exists in the database ");
            logger.error("with query {}: {}", emailExistsQuery, e.getMessage());
            throw new RuntimeException(e);
        }


//        String emailExistsQuery = "Select 1 from LOGIN_CREDENTIALS WHERE EMAIL_ID ='"+emailId+"'";
//
//        try {
//            logger.info("Checking email exists in database {}", emailId);
////            PreparedStatement preparedStatement = connection.prepareStatement(emailExistsQuery);
////            preparedStatement.setString(1,emailId);
//
//            Statement statement  = connection.createStatement();
//
//
////            ResultSet resultSet = statement.executeQuery(emailExistsQuery);
//
////            int rows = preparedStatement.executeUpdate();
//             if (statement.executeUpdate(emailExistsQuery) > 0){
//                 return true;
//             }
//             logger.info("No such  email id  exists in the database");
//        } catch (SQLException e) {
//            logger.info("Checking email exists in database {} with query {} ", emailId,emailExistsQuery);
//            logger.warn("Error occurred in retrieving mail id ");
//            logger.error(e.getMessage());
//            throw new RuntimeException(e);
//        }
//
//        return false;
    }

    @Override
    public String getPasswordByEmailId(String emailId) {

        String password = "";


        try (PreparedStatement preparedStatement = connection.prepareStatement(getPasswordWithEmailQuery)) {
            // Set the emailId parameter
            preparedStatement.setString(1, emailId);

            // Execute the query and get the result set
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Retrieve the password from the result set
                    password = resultSet.getString("PASSWORD");
                    logger.info("password from db : {}", password);
                } else {
                    // Handle the case where no password was found (optional)
                    logger.warn("No password found for email ID: {}", emailId);
                }
            }
        } catch (SQLException e) {
            // Log and handle the SQL exception
            logger.error("Error occurred while retrieving password for email ID {}: {}", emailId, e.getMessage());
            throw new RuntimeException(e);
        }

        return password;

//        String password = "";
//
//        String getPasswordWithEmailQuery = "SELECT PASSWORD FROM LOGIN_CREDENTIALS WHERE EMAIL_ID = '"+emailId+"'";
//        try{
//            Statement statement  = connection.createStatement();
////            preparedStatement.setString(1,emailId);
//
//            ResultSet resultSet = statement.executeQuery(getPasswordWithEmailQuery);
//            resultSet.next();
//            password = resultSet.getString("PASSWORD");
//        } catch (SQLException e) {
//            logger.error(e.getMessage());
//            throw new RuntimeException(e);
//        }
//        return password;
    }
}
