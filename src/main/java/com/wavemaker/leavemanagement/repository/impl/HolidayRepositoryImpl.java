package com.wavemaker.leavemanagement.repository.impl;

import com.wavemaker.leavemanagement.config.DatabaseConnectionManager;
import com.wavemaker.leavemanagement.exception.ErrorResponse;
import com.wavemaker.leavemanagement.repository.HolidayRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HolidayRepositoryImpl implements HolidayRepository {

    private final Connection connection;

    private static final Logger logger = LoggerFactory.getLogger(HolidayRepositoryImpl.class);

    private final String getHolidaysQuery = "SELECT * FROM HOLIDAYS " +
            "WHERE HOLIDAY_DATE >= CURDATE() " +
            "ORDER BY HOLIDAY_DATE";

    public HolidayRepositoryImpl() {
        try {
            this.connection = DatabaseConnectionManager.getInstance().getConnection();
        } catch (SQLException e) {
            logger.warn("Error in getting connection objet from HolidayRepositoryImpl class");
            throw new RuntimeException(e);
        }
    }


    @Override
    public String getHolidays() {

        JSONArray holidaysJsonArray = new JSONArray();
        logger.info("Extracting all holidays from database");
        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(getHolidaysQuery);


            while (resultSet.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("holidayName", resultSet.getString("HOLIDAY_NAME"));
                jsonObject.put("day", resultSet.getString("DAY"));
                jsonObject.put("holidayDate", resultSet.getDate("HOLIDAY_DATE"));
                holidaysJsonArray.put(jsonObject);
            }

            logger.info("Extracted holidays from the Holidays table as {}", holidaysJsonArray);

        } catch (SQLException e) {
            logger.error("Error occurred while extracting holidays from the table {}", e.getMessage());
            logger.error("Complete exception {}", String.valueOf(e.getNextException()));
            throw new ErrorResponse(e.getMessage());
        }

        return holidaysJsonArray.toString();
    }
}
