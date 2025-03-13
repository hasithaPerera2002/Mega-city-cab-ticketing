package org.icbt.hasitha.megacity.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getHeader("Authorization");
        LOGGER.info("token: {}", token);
        if (token == null || token.trim().isEmpty()) {
            this.sendResponse.SendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Authorization token is required");
            return;
        }

        token = token.trim();
        boolean isValid = JwtUtil.isTokenValidAndNotExpired(token);

        if (!isValid) {
            this.sendResponse.SendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Invalid token");
        }

        BufferedReader reader = req.getReader();
        TripDetailsDTO tripDetailsDTO = gson.fromJson(reader, TripDetailsDTO.class);
        LOGGER.info("tripDetailsDTO: {}", tripDetailsDTO);
        ValidationResultDTO validationResultDTO = validateTripDetails(tripDetailsDTO, resp);

        if (!validationResultDTO.isValid()) {
            this.sendResponse.SendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Invalid trip details", validationResultDTO.getMessage());
            return;
        }
        LOGGER.info("validationResultDTO: {}", validationResultDTO);
        ResultDTO<Boolean> resultDTO = bookingService.saveBooking(tripDetailsDTO);

        if (resultDTO.getData()) {
            this.sendResponse.SendJsonResponse(resp, HttpServletResponse.SC_CREATED, "success", "Booking saved successfully", null);
        } else {
            this.sendResponse.SendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error saving booking", resultDTO.getMessage());
        }


    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }


    public ValidationResultDTO validateTripDetails(TripDetailsDTO trip, HttpServletResponse resp) throws IOException {

        if (!trip.getCustomerEmail().matches("^[a-zA-Z0-9]{6,12}$")) {
            return new ValidationResultDTO(false, "error", "Invalid customer ID");
        }
        if (!trip.getDate().toString().matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            return new ValidationResultDTO(false, "error", "Invalid date format");
        }
        if (!trip.getDestination().matches("^[a-zA-Z ]+$")) {
            return new ValidationResultDTO(false, "error", "Invalid destination");
        }
        if (!trip.getStartLocation().matches("^[a-zA-Z ]+$")) {
            return new ValidationResultDTO(false, "error", "Invalid start location");
        }
        if (!trip.getStartTime().matches("^([01]\\d|2[0-3]):[0-5]\\d$")) {
            return new ValidationResultDTO(false, "error", "Invalid start time");
        }
        if (!trip.getFare().matches("^\\d+(\\.\\d{1,2})?$")) {
            return new ValidationResultDTO(false, "error", "Invalid fare");
        }
        if (!trip.getDistance().matches("^\\d+(\\.\\d{1,2})?$")) {
            return new ValidationResultDTO(false, "error", "Invalid distance");
        }
        if (!String.valueOf(trip.getPassengerCount()).matches("^[1-9]\\d*$")) {
            return new ValidationResultDTO(false, "error", "Invalid passenger count");
        }
        boolean isValid = Arrays.stream(VehicleTypes.values())
                .anyMatch(type -> type.name().equalsIgnoreCase(trip.getVehicleType().getDisplayName()));
        if (!isValid) {
            return new ValidationResultDTO(false, "error", "Invalid vehicle type");
        }

        return new ValidationResultDTO(true, "success", "Valid trip details");
    }



}
