package org.icbt.hasitha.megacity.service;

import jakarta.servlet.http.HttpServletResponse;
import org.icbt.hasitha.megacity.dto.ResultDTO;
import org.icbt.hasitha.megacity.dto.TripDetailsDTO;
import org.icbt.hasitha.megacity.repository.BookingRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookingService {
    private final Logger LOGGER = LoggerFactory.getLogger(BookingService.class);
    private BookingRepo bookingRepo;

    public BookingService() {
        this.bookingRepo = new BookingRepo();
    }

    public ResultDTO<Boolean> saveBooking(TripDetailsDTO tripDetailsDTO) {
        LOGGER.info("Booking saved successfully");
        try {
            boolean flag = bookingRepo.saveBooking(tripDetailsDTO);
            return new ResultDTO<Boolean>("Booking saved successfully", HttpServletResponse.SC_CREATED, flag);
        } catch (Exception e) {
            LOGGER.error("Error saving booking: {}", e.getMessage());
            return new ResultDTO<Boolean>(e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false);
        }


    }

}
