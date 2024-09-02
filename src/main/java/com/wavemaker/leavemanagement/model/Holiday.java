package com.wavemaker.leavemanagement.model;

import java.sql.Date;

public class Holiday {
    String holidayName;
    String day;
    Date holidayDate;

    public Holiday() {
    }

    public Holiday(String holidayName, String day, Date holidayDate) {
        this.holidayName = holidayName;
        this.day = day;
        this.holidayDate = holidayDate;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Date getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(Date holidayDate) {
        this.holidayDate = holidayDate;
    }

    @Override
    public String toString() {
        return "Holiday{" +
                "holidayName='" + holidayName + '\'' +
                ", day='" + day + '\'' +
                ", holidayDate=" + holidayDate +
                '}';
    }
}
