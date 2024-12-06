package com.wavemaker.leavemanagement.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


// Database connection

public class DatabaseConnectionManager {

    private final Logger logger = LoggerFactory.getLogger(DatabaseConnectionManager.class);
    private static DatabaseConnectionManager databaseConnectionManager;
    private Connection connection;

    private DatabaseConnectionManager() {
        Properties properties = new Properties();
        try {
            try (InputStream inputStream = DatabaseConnectionManager.class
                    .getClassLoader().getResourceAsStream("database/db.properties")) {
                if (inputStream == null) {
                    logger.info("Unable to find the database properties file.");
                }
                properties.load(inputStream);

            } catch (IOException ioException) {
                logger.error("Unable to load the database properties file");
                logger.error("Error : {}", ioException.getMessage());
            }
            Class.forName(properties.getProperty("db.driver"));
            this.connection = DriverManager.getConnection(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.user"),
                    properties.getProperty("db.password")
            );

        } catch (ClassNotFoundException | SQLException exception) {
            exception.printStackTrace();
            logger.error("exception occurred : {}", exception.getMessage());
        }

    }

    public static synchronized DatabaseConnectionManager getInstance() throws SQLException {
        if (databaseConnectionManager == null || databaseConnectionManager.getConnection().isClosed()) {
            databaseConnectionManager = new DatabaseConnectionManager();
        }
        return databaseConnectionManager;
    }

    public Connection getConnection() {
        return connection;
    }


}

