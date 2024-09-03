package com.wavemaker.leavemanagement.service.impl;

import com.wavemaker.leavemanagement.factory.RepositoryFactory;
import com.wavemaker.leavemanagement.repository.HolidayRepository;
import com.wavemaker.leavemanagement.service.HolidaysService;

public class HolidaysServiceImpl implements HolidaysService {
    private HolidayRepository holidayRepository = null;

    public HolidaysServiceImpl() {
        holidayRepository = new RepositoryFactory().getHolidayRepository("HolidayRepository");
    }

    public String getAllHolidays() {
        return holidayRepository.getHolidays();
    }
}
