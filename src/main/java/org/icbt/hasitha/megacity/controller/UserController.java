package org.icbt.hasitha.megacity.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.icbt.hasitha.megacity.dto.*;
import org.icbt.hasitha.megacity.service.UserService;
import org.icbt.hasitha.megacity.util.JwtUtil;
import org.icbt.hasitha.megacity.util.SendResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@WebServlet(urlPatterns = "/api/user/*", name = "UserController")
public class UserController extends HttpServlet {
    private final UserService userService;
    private final Logger LOGGER = Logger.getLogger(UserController.class.getName());
    private final Gson gson = new Gson();
    private final SendResponse sendResponse = new SendResponse();
    public UserController() {
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        switch (pathInfo) {
            case "/login":
                loginUser(req, resp);
                break;
            case "/signup":
                registerUser(req, resp);
                break;
            default:
                sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_NOT_FOUND, "error", "Invalid API endpoint", "Invalid API endpoint");
                break;
        }
    }

    public void loginUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            LOGGER.info("Login request received");
            BufferedReader reader = req.getReader();
            LoginDTO loginRequest = gson.fromJson(reader, LoginDTO.class);
            LOGGER.info("User" + loginRequest);
            LOGGER.info("Email: " + loginRequest.getEmail());

            LoginResponseDTO loginResponseDTO = userService.validateUser(loginRequest.getEmail(), loginRequest.getPassword());
            LOGGER.info("Login response: " + loginResponseDTO);
            if (loginResponseDTO.isValid()) {
                LOGGER.info("Login successful");
                String token = JwtUtil.generateToken(loginResponseDTO.getUsername(), loginResponseDTO.getRole());
                LOGGER.info("Token generated: " + token);
                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("message", "Login successful");
                responseJson.addProperty("token", token);

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                resp.setHeader("Authorization", "Bearer " + token);

                resp.getWriter().write(gson.toJson(responseJson));
                resp.getWriter().flush();
                LOGGER.info("Response sent" + resp.getHeader("Authorization"));
                resp.getWriter().close();
            } else {
                this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Invalid credentials", loginResponseDTO.getMessage());
            }
        } catch (IOException e) {
            LOGGER.severe("Error occurred: " + e.getMessage());
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "An error occurred", "Login failed");
            e.printStackTrace();
        }
    }

    public void registerUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try{
        Gson gson = new Gson();
        SignUpDTO userDto = gson.fromJson(req.getReader(), SignUpDTO.class);

        ValidationResultDTO validationResult = validateUser(userDto);
        LOGGER.info("Validation result: " + validationResult.toString());
        if (!validationResult.isValid()) {
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "An error occurred", validationResult.getMessage());
            return;
        }
          boolean isExistingUser = userService.isExistingUser(userDto.getEmail());

        if (isExistingUser) {
            LOGGER.info("User already exists");
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "User already exists", "User already exists");
            return;
        }
        boolean isRegistered = userService.registerUser(userDto);

        if (isRegistered) {
            LOGGER.info("User registered successfully");
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_OK, "message", "Registration successful", null);
        } else {
            LOGGER.info("User registration failed");
            this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "An error occurred", "Registration failed");
        }
    } catch (IOException e) {
        LOGGER.severe("Error occurred: " + e.getMessage());
        this.sendResponse.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "An error occurred", "Registration failed");
        e.printStackTrace();
        }
    }

    private ValidationResultDTO validateUser(SignUpDTO userDto) {
        if (!isValidUsername(userDto.getUsername())) {
            return new ValidationResultDTO(false, "Invalid username", "username");
        }

        if (!isValidPassword(userDto.getPassword())) {
            return new ValidationResultDTO(false, "Invalid password", "password");
        }

        if (!isValidEmail(userDto.getEmail())) {
            return new ValidationResultDTO(false, "Invalid email", "email");
        }

        if (!isValidPhone(userDto.getPhone())) {
            return new ValidationResultDTO(false, "Invalid phone number", "phone");
        }

        if (userDto.getAddress() == null || userDto.getAddress().isEmpty()) {
            return new ValidationResultDTO(false, "Invalid address", "address");
        }

        return new ValidationResultDTO(true, "User details are valid", null);
    }

    private boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) return false;
        String usernameRegex = "^[a-zA-Z0-9_]{3,20}$";
        Pattern pattern = Pattern.compile(usernameRegex);
        return pattern.matcher(username).matches();
    }

    private boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) return false;
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        return pattern.matcher(password).matches();
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) return false;
        String phoneRegex = "^[0-9]{10}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        return pattern.matcher(phone).matches();
    }

}
