package org.icbt.hasitha.megacity.service;

import jakarta.servlet.http.HttpServletResponse;
import org.icbt.hasitha.megacity.dto.ResultDTO;
import org.icbt.hasitha.megacity.dto.TripDetailsDTO;
import org.icbt.hasitha.megacity.entity.Trip;
import org.icbt.hasitha.megacity.repository.BookingRepo;
import org.icbt.hasitha.megacity.service.interfaces.IBookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class BookingService implements IBookingService {
    private final Logger LOGGER = LoggerFactory.getLogger(BookingService.class);
    private BookingRepo bookingRepo =new BookingRepo();


    @Override
    public List<TripDetailsDTO> getAllBookings(String userId) {
        LOGGER.info("Fetching all bookings for user: {}", userId != null ? userId : "ALL");
        try {
            List<Trip> trips = bookingRepo.getTrips(userId);
            return trips.stream().map(
                    trip -> new TripDetailsDTO(
                            trip.getTripId(),
                            trip.getVehicleId(),
                            trip.getCustomerId(),
                            trip.getDate(),
                            trip.getDestination(),
                            trip.getStartTime(),
                            String.valueOf(trip.getFare()),
                            trip.getDistance(),
                            trip.getPassengerCount(),
                            trip.getStartLocation())).toList();

        } catch (Exception e) {
            LOGGER.error("Error fetching bookings: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
    @Override
    public ResultDTO<Boolean> updateBooking(TripDetailsDTO tripDetailsDTO) {
        LOGGER.info("Updating booking: {}", tripDetailsDTO);
        try {
            boolean flag = bookingRepo.updateBooking(tripDetailsDTO);
            if (flag) {
                return new ResultDTO<>("Booking updated successfully", HttpServletResponse.SC_OK, true);
            } else {
                return new ResultDTO<>("Booking not found or could not be updated", HttpServletResponse.SC_NOT_FOUND, false);
            }
        } catch (Exception e) {
            LOGGER.error("Error updating booking: {}", e.getMessage());
            return new ResultDTO<>(e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false);
        }
    }
    @Override
    public ResultDTO<Boolean> deleteBooking(UUID bookingId) throws RuntimeException {
        LOGGER.info("Deleting booking with ID: {}", bookingId);
        try {
            boolean flag = bookingRepo.deleteBooking(bookingId);
            LOGGER.info(String.valueOf(flag));
            if (flag) {
                LOGGER.info("Booking deleted successfully");
                return new ResultDTO<>("Booking deleted successfully", HttpServletResponse.SC_OK, true);
            } else {
                LOGGER.error("Booking not found or could not be deleted: {}", bookingId);
                return new ResultDTO<>("Booking not found or could not be deleted", HttpServletResponse.SC_NOT_FOUND, false);
            }
        } catch (Exception e) {
            LOGGER.error("Error deleting booking: {}", e.getMessage());
            return new ResultDTO<>(e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false);
        }
    }


    @Override
    public ResultDTO<Boolean> save(TripDetailsDTO tripDetailsDTO) {
        LOGGER.info("BookingService.saveBooking: {}", tripDetailsDTO);
        try {
            boolean flag = bookingRepo.saveBooking(tripDetailsDTO);
            if (flag) {
                String subject = "Booking Confirmation";
                String message = "Dear Customer,\n\nYour booking has been successfully placed!\n" +
                        "Trip ID: " + tripDetailsDTO.getTripId() + "\n" +
                        "Vehicle Type: " + tripDetailsDTO.getVehicleType() + "\n" +
                        "Destination: " + tripDetailsDTO.getDestination() + "\n" +
                        "Start Time: " + tripDetailsDTO.getStartTime() + "\n\n" +
                        "Thank you for choosing our service.";

                EmailService.sendBookingConfirmation(tripDetailsDTO.getCustomerEmail(), subject, message);
            }
            return new ResultDTO<Boolean>("Booking saved successfully", HttpServletResponse.SC_CREATED, flag);
        } catch (Exception e) {
            LOGGER.error("Error saving booking: {}", e.getMessage());
            return new ResultDTO<Boolean>(e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false);
        }


    }


}
