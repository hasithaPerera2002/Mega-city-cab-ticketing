package org.icbt.hasitha.megacity.controller;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.icbt.hasitha.megacity.dto.ResultDTO;
import org.icbt.hasitha.megacity.dto.TripDetailsDTO;
import org.icbt.hasitha.megacity.dto.ValidationResultDTO;
import org.icbt.hasitha.megacity.service.BookingService;
import org.icbt.hasitha.megacity.util.JwtUtil;
import org.icbt.hasitha.megacity.util.SendResponse;
import org.icbt.hasitha.megacity.util.enums.VehicleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@WebServlet(urlPatterns = "/api/booking/*", name = "BookingController")
public class BookingController extends HttpServlet {
    private final Logger LOGGER = LoggerFactory.getLogger(BookingController.class);
    private final Gson gson = new Gson();
    private final BookingService bookingService;
    private final SendResponse sendResponse;

    public BookingController() {
        this.sendResponse = new SendResponse();
        this.bookingService = new BookingService();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String token = req.getHeader("Authorization");
        LOGGER.info("token: {}", token);

        if (token == null || token.trim().isEmpty()) {
            LOGGER.info("token is empty");
            sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Authorization token is required");
            return;
        }
        LOGGER.info("token: {}", token);
        token = token.substring(7).trim();
        if (JwtUtil.isTokenExpired(token)) {
            LOGGER.info("token is expired");
            sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Invalid token");
            return;
        }
        String userId = req.getParameter("userEmail");
        try {
            List<TripDetailsDTO> bookings = bookingService.getAllBookings(userId);
            sendResponse.sendJsonResponseData(resp, HttpServletResponse.SC_OK, "success", "Bookings retrieved successfully", null, gson.toJson(bookings));

        } catch (Exception e) {
            sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Failed to retrieve booking", e.getMessage());
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getHeader("Authorization");
        LOGGER.info("token: {}", token);
        try {
            if (token == null || token.trim().isEmpty()) {
                this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Authorization token is required");
                return;
            }
            token = token.substring(7);
            token = token.trim();
            boolean isValid = JwtUtil.isTokenExpired(token);

            if (isValid) {
                this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Invalid token");
            }

            BufferedReader reader = req.getReader();
            TripDetailsDTO tripDetailsDTO = gson.fromJson(reader, TripDetailsDTO.class);
            LOGGER.info("tripDetailsDTO: {}", tripDetailsDTO);
            ValidationResultDTO validationResultDTO = validateTripDetails(tripDetailsDTO, resp);
            LOGGER.info("validationResultDTO: {}", validationResultDTO);
            if (!validationResultDTO.isValid()) {
                this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "error", validationResultDTO.getMessage(), validationResultDTO.getError());
                return;
            }
            LOGGER.info("validationResultDTO: {}", validationResultDTO);
            ResultDTO<Boolean> resultDTO = bookingService.save(tripDetailsDTO);

            if (resultDTO.getData()) {
                this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_CREATED, "success", "Booking saved successfully", null);
            } else {
                this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error saving booking", resultDTO.getMessage());
            }
        } catch (IOException | JsonIOException | JsonSyntaxException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getHeader("Authorization");
        LOGGER.info("token: {}", token);

        if (token == null || token.trim().isEmpty()) {
            sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Authorization token is required");
            return;
        }

        token = token.substring(7).trim();
        if (JwtUtil.isTokenExpired(token)) {
            sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Invalid token");
            return;
        }

        try {
            BufferedReader reader = req.getReader();
            TripDetailsDTO tripDetailsDTO = gson.fromJson(reader, TripDetailsDTO.class);
            LOGGER.info("tripDetailsDTO: {}", tripDetailsDTO);

            if (tripDetailsDTO.getTripId() == null) {
                sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Trip ID is required for update", null);
                return;
            }

            ResultDTO<Boolean> resultDTO = bookingService.updateBooking(tripDetailsDTO);
            if (resultDTO.getData()) {
                sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_OK, "success", "Booking updated successfully", null);
            } else {
                sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error updating booking", resultDTO.getMessage());
            }
        } catch (Exception e) {
            sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Failed to process request", e.getMessage());
        }
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getHeader("Authorization");
        LOGGER.info("token: {}", token);

        if (token == null || token.trim().isEmpty()) {
            sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Authorization token is required");
            return;
        }

        token = token.substring(7).trim();
        if (JwtUtil.isTokenExpired(token)) {
            sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Invalid token");
            return;
        }

        String bookingId = req.getParameter("id");
        if (bookingId == null) {
            sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Booking ID is required for deletion", null);
            return;
        }

        try {
            ResultDTO<Boolean> resultDTO = bookingService.deleteBooking(UUID.fromString(bookingId));
            if (resultDTO.getData()) {
                sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_OK, "success", "Booking deleted successfully", null);
            } else {
                sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error deleting booking", resultDTO.getMessage());
            }
        } catch (Exception e) {
            LOGGER.info("Error deleting booking: {}", e.getMessage());
            sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Failed to process request", e.getMessage());
        }
    }


    public ValidationResultDTO validateTripDetails(TripDetailsDTO trip, HttpServletResponse resp) throws IOException {

        if ( trip.getCustomerEmail() == null || !trip.getCustomerEmail().matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$") ) {
            return new ValidationResultDTO(false, "error", "Invalid customer email");
        }

        if (trip.getDestination().isEmpty()) {
            return new ValidationResultDTO(false, "error", "Invalid destination");
        }
        if (trip.getStartLocation().isEmpty()) {
            return new ValidationResultDTO(false, "error", "Invalid start location");
        }
        if ( trip.getStartTime() == null || !trip.getStartTime().matches("^([01]\\d|2[0-3]):[0-5]\\d$")) {
            return new ValidationResultDTO(false, "error", "Invalid start time");
        }
        if (trip.getFare() == null ||!trip.getFare().matches("^\\d+(\\.\\d{1,12})?$")) {
            return new ValidationResultDTO(false, "error", "Invalid fare");
        }
        if ( trip.getDistance() == null ||  !trip.getDistance().matches("^\\d+(\\.\\d{1,12})?$")) {
            return new ValidationResultDTO(false, "error", "Invalid distance");
        }
        if (!String.valueOf(trip.getPassengerCount()).matches("^[1-9]\\d*$")) {
            return new ValidationResultDTO(false, "error", "Invalid passenger count");
        }
        boolean isValid = Arrays.stream(VehicleTypes.values())
                .anyMatch(type -> type.name().equalsIgnoreCase(trip.getVehicleType().toString()));
        if (!isValid) {
            return new ValidationResultDTO(false, "error", "Invalid vehicle type");
        }

        return new ValidationResultDTO(true, "success", "Valid trip details");
    }


}
