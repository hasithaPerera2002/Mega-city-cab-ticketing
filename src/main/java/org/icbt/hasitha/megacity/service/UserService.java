package org.icbt.hasitha.megacity.service;

import org.icbt.hasitha.megacity.dto.LoginResponseDTO;
import org.icbt.hasitha.megacity.dto.SignUpDTO;
import org.icbt.hasitha.megacity.entity.User;
import org.icbt.hasitha.megacity.repository.UserRepo;
import org.icbt.hasitha.megacity.service.interfaces.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class UserService implements IUserService {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private final UserRepo userRepo = new UserRepo();
    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);


    @Override
    public LoginResponseDTO validateUser(String email, String password) {
        try {
            if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
                return new LoginResponseDTO(false, "Invalid email format", "email");
            }

            if (password == null || password.trim().isEmpty()) {
                return new LoginResponseDTO(false, "Password is required", "password");
            }

            User user = this.userRepo.findUserByEmail(email);
            if (user != null && user.getPassword_hash().equals(password)) {
                LOGGER.info("User is valid");
                return new LoginResponseDTO(true, "User found", "email", user.getRole(), user.getEmail());
            } else {
                LOGGER.info("User is invalid");
                return new LoginResponseDTO(false, "User not found", "email");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean registerUser(SignUpDTO signUpDTO) {

        try {
            LOGGER.info("Registering user: {}", signUpDTO.toString());
            return userRepo.saveUser(signUpDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


    public boolean isExistingUser(String email) {
        return userRepo.checkUserExists(email);

    }
}
