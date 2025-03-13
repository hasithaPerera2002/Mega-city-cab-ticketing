package org.icbt.hasitha.megacity.controller;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet (name = "VehicleController", urlPatterns = "/api/vehicle/*")
public class VehicleController extends HttpServlet {
    private final Logger LOGGER = LoggerFactory.getLogger(VehicleController.class);
    private final Gson gson = new Gson();
    private final SendResponse sendResponse = new SendResponse();
    private final VehicleService vehicleService = new VehicleService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("Vehicle request received");
        BufferedReader reader = req.getReader();
        VehicleDTO vehicleDTO = gson.fromJson(reader, VehicleDTO.class);
        LOGGER.info("Vehicle" + vehicleDTO);
        String token = req.getHeader("Authorization");
        LOGGER.info("token: {}",token);
        if (token == null || token.trim().isEmpty()) {
            this.sendResponse.SendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Authorization token is required");
            return;
        }

        token = token.trim();
        boolean isValid = JwtUtil.isTokenValidAndNotExpired(token);

        if (!isValid) {
            this.sendResponse.SendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Invalid token");
        }
       ValidationResultDTO validationResultDTO = validateVehicle(vehicleDTO);
        if (validationResultDTO.isValid()) {
            this.sendResponse.SendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Invalid vehicle data", validationResultDTO.getError());
            return;
        }

       ResultDTO<Boolean> resultDTO = vehicleService.addVehicle(vehicleDTO);

        if (resultDTO.getData()) {
            this.sendResponse.SendJsonResponse(resp, HttpServletResponse.SC_CREATED, "success", "Vehicle added successfully", null);
        } else {
            this.sendResponse.SendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Failed to add vehicle", resultDTO.getMessage());
        }

    }

    public ValidationResultDTO validateVehicle(VehicleDTO vehicleDTO) {
        if (vehicleDTO.getVehicle_id() == null) {
            return new ValidationResultDTO(false, "Vehicle ID is required", "Missing vehicle ID");
        }

        if (vehicleDTO.getVehicle_number() == null || vehicleDTO.getVehicle_number().isEmpty()) {
            return new ValidationResultDTO(false, "Vehicle number is required", "Missing vehicle number");
        }

        if (vehicleDTO.getVehicle_type() == null) {
            return new ValidationResultDTO(false, "Vehicle type is required", "Missing vehicle type");
        }

        if (vehicleDTO.getVehicle_model() == null || vehicleDTO.getVehicle_model().isEmpty()) {
            return new ValidationResultDTO(false, "Vehicle model is required", "Missing vehicle model");
        }

        if (vehicleDTO.getVehicle_color() == null || vehicleDTO.getVehicle_color().isEmpty()) {
            return new ValidationResultDTO(false, "Vehicle color is required", "Missing vehicle color");
        }

        if (vehicleDTO.getVehicle_image() != null && !isValidImageFormat(vehicleDTO.getVehicle_image())) {
            return new ValidationResultDTO(false, "Invalid vehicle image format", "Invalid image format");
        }

        if (vehicleDTO.getStatus() == null) {
            return new ValidationResultDTO(false, "Vehicle status is required", "Missing vehicle status");
        }

        if (vehicleDTO.getDriver_id() == null) {
            return new ValidationResultDTO(false, "Driver ID is required", "Missing driver ID");
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

    private boolean isValidImageFormat(String image) {
        return image.matches("^(http(s)?://)?[\\w]+(\\.[\\w]+)+.*$") || image.matches("^[A-Za-z0-9+/=]+$");
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

}
