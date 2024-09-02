package com.wavemaker.leavemanagement.service.impl;

import com.wavemaker.leavemanagement.repository.impl.HolidayRepositoryImpl;

public class HolidaysServiceImpl {
    public String getAllHolidays() {
        return new HolidayRepositoryImpl().getHolidays();
    }
}
