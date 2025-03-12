package org.icbt.hasitha.megacity.service;

import org.icbt.hasitha.megacity.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookingService {
    private final Logger LOGGER = LoggerFactory.getLogger(BookingService.class);

    public BookingService() {
    }

    public ResultDTO<Boolean> saveBooking() {
        LOGGER.info("Booking saved successfully");

        return new ResultDTO<>("Booking saved successfully", "200", true);
    }

}
