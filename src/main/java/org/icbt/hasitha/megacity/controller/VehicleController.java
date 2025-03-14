package org.icbt.hasitha.megacity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.icbt.hasitha.megacity.dto.*;
import org.icbt.hasitha.megacity.service.VehicleService;
import org.icbt.hasitha.megacity.util.JwtUtil;
import org.icbt.hasitha.megacity.util.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet (name = "VehicleController", urlPatterns = "/api/vehicle/*")
public class VehicleController extends HttpServlet {
    private final Logger LOGGER = LoggerFactory.getLogger(VehicleController.class);
    private final Gson gson = new Gson();
    public final SendResponse sendResponse = new SendResponse();
    public final VehicleService vehicleService = new VehicleService();
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        LOGGER.info("Vehicle request received");
        String jwtToken = req.getHeader("Authorization");
        if (jwtToken == null || jwtToken.trim().isEmpty()) {
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Authorization token is required");
            return;
        }
        String token = jwtToken.substring(7);

        token = token.trim();
        boolean isValid = JwtUtil.isTokenExpired(token);

        if (isValid) {
            LOGGER.info("token expired");
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Invalid token");
        }
        VehicleDTO vehicleDTO = gson.fromJson(reader, VehicleDTO.class);

        if (!JwtUtil.isAdmin(token)) {
            LOGGER.info("Invalid token");
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Admin access required");
            return;
        }

       ValidationResultDTO validationResultDTO = validateVehicle(vehicleDTO);
        if (!validationResultDTO.isValid()) {
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Invalid vehicle data", validationResultDTO.getError());
            return;
        }

       ResultDTO<Boolean> resultDTO = vehicleService.addVehicle(vehicleDTO);

        if (resultDTO.getData()) {
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_CREATED, "success", "Vehicle added successfully", null);
        } else {
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Failed to add vehicle", resultDTO.getMessage());
        }

    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("Vehicle request received");
        BufferedReader reader = req.getReader();
        String jwtToken = req.getHeader("Authorization");

        LOGGER.info("token:{}",jwtToken);
        if (jwtToken == null || jwtToken.trim().isEmpty()) {
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Authorization token is required");
            return;
        }
        String token = jwtToken.substring(7);
        token = token.trim();
        boolean isValid = JwtUtil.isTokenExpired(token);
        if (isValid) {
            LOGGER.info("Invalid token");
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Invalid token");
        }

        try {
            VehicleDTO[] vehicleDTOS= vehicleService.getVehicles();
            String jsonResponse = new ObjectMapper().writeValueAsString(vehicleDTOS);
            LOGGER.info("Generated JSON Response: {}", jsonResponse);

            this.sendResponse.sendJsonResponseData(resp, HttpServletResponse.SC_OK, "success", "Vehicle data retrieved successfully","", jsonResponse);
        } catch (IOException e) {
            this.sendResponse.sendJsonResponse( resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Failed to retrieve vehicle data", e.getMessage());
        }
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String jwtToken = req.getHeader("Authorization");
        if (jwtToken == null || jwtToken.trim().isEmpty()) {
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Authorization token is required");
            return;
        }

        String token = jwtToken.substring(7).trim();
        if (JwtUtil.isTokenExpired(token)) {
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Invalid token");
            return;
        }

        if (!JwtUtil.isAdmin(token)) {
            LOGGER.info("Unauthorized update attempt");
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Admin access required");
            return;
        }
        LOGGER.info("Vehicle update request received");
        BufferedReader reader = req.getReader();
        VehicleDTO vehicleDTO = gson.fromJson(reader, VehicleDTO.class);
        LOGGER.info("Vehicle update details: " + vehicleDTO);

        ValidationResultDTO validationResultDTO = validateVehicleUpdate(vehicleDTO);
        if (!validationResultDTO.isValid()) {
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Invalid vehicle data", validationResultDTO.getError());
            return;
        }

        ResultDTO<Boolean> resultDTO = vehicleService.updateVehicle(vehicleDTO);
        if (resultDTO.getData()) {
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_OK, "success", "Vehicle updated successfully", null);
        } else {
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Failed to update vehicle", resultDTO.getMessage());
        }
    }



    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("Vehicle delete request received");

        String jwtToken = req.getHeader("Authorization");
        if (jwtToken == null || jwtToken.trim().isEmpty()) {
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Authorization token is required");
            return;
        }

        String token = jwtToken.substring(7).trim();
        if (JwtUtil.isTokenExpired(token)) {
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Invalid token");
            return;
        }

        if (!JwtUtil.isAdmin(token)) {
            LOGGER.info("Unauthorized delete attempt");
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Admin access required");
            return;
        }

        String vehicleId = req.getParameter("vehicleId");
        if (vehicleId == null || vehicleId.trim().isEmpty()) {
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Invalid request", "Vehicle ID is required");
            return;
        }

        ResultDTO<Boolean> resultDTO = vehicleService.deleteVehicle(UUID.fromString(vehicleId));
        if (resultDTO.getData()) {
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_OK, "success", "Vehicle deleted successfully", null);
        } else {
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Failed to delete vehicle", resultDTO.getMessage());
        }
    }


    public ValidationResultDTO validateVehicle(VehicleDTO vehicleDTO) {

        if (vehicleDTO.getVehicle_number() == null || vehicleDTO.getVehicle_number().isEmpty()) {
            LOGGER.info("Vehicle number is required");
            return new ValidationResultDTO(false, "Vehicle number is required", "Missing vehicle number");
        }

        if (vehicleDTO.getVehicle_type() == null) {
            return new ValidationResultDTO(false, "Vehicle type is required", "Missing vehicle type");
        }

        if (vehicleDTO.getStatus() == null) {
            return new ValidationResultDTO(false, "Vehicle status is required", "Missing vehicle status");
        }


        if (vehicleDTO.getDriver_name() == null || vehicleDTO.getDriver_name().isEmpty()) {
            return new ValidationResultDTO(false, "Driver name is required", "Missing driver name");
        }

        if (vehicleDTO.getDriver_contact() == null || !isValidPhoneNumber(vehicleDTO.getDriver_contact())) {
            return new ValidationResultDTO(false, "Invalid driver contact number", "Invalid phone number");
        }

        if (vehicleDTO.getDriver_nic() == null || !isValidNIC(vehicleDTO.getDriver_nic())) {
            return new ValidationResultDTO(false, "Invalid driver NIC", "Invalid NIC format");
        }

        if (vehicleDTO.getDriver_email() == null || !isValidEmail(vehicleDTO.getDriver_email())) {
            return new ValidationResultDTO(false, "Invalid driver email", "Invalid email format");
        }

        return new ValidationResultDTO(true, "Vehicle is valid", "No errors");
    }

    private boolean isValidPhoneNumber(String phone) {
        String phoneRegex = "^\\+?[0-9]{10,15}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    private boolean isValidNIC(String nic) {
        String nicRegex = "^[0-9]{9}[VX]$";
        return nic.matches(nicRegex);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private ValidationResultDTO validateVehicleUpdate(VehicleDTO vehicleDTO) {
        if (vehicleDTO.getVehicle_number() == null || vehicleDTO.getVehicle_number().isEmpty()) {
            return new ValidationResultDTO(false, "Vehicle number is required", "Missing vehicle number");
        }
        return new ValidationResultDTO(true, "Vehicle is valid", "No errors");
    }

}
