package org.icbt.hasitha.megacity.repository;

import org.icbt.hasitha.megacity.dto.SignUpDTO;
import org.icbt.hasitha.megacity.entity.User;
import org.icbt.hasitha.megacity.repository.interfaces.IUserRepo;
import org.icbt.hasitha.megacity.util.db.DBConnection;
import org.icbt.hasitha.megacity.util.enums.RoleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserRepo implements IUserRepo {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepo.class);

    @Override
    public User findUserByEmail(String email) throws SQLException {
        LOGGER.info("Finding user by email: " + email);
        String query = "SELECT user_id, email, password_hash, username ,role FROM users WHERE email = ?";
        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            LOGGER.info("Executing query: " + stmt.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LOGGER.info("User found");
                return new User(rs.getObject("user_id", UUID.class), rs.getString("username"), rs.getString("password_hash"), rs.getString("email"), RoleType.valueOf(rs.getString("role")));
            }
            LOGGER.info("User not found");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public boolean saveUser(SignUpDTO signUpDTO) {
        LOGGER.info("Saving user: " + signUpDTO);

        String query = "INSERT INTO users (user_id, username, password_hash, email, role, phone_number, created_at, address) " +
                "VALUES (?, ?, ?, ?, ?, ?, now(), ?)";

        try (Connection connection = DBConnection.getInstance().getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setObject(1, UUID.randomUUID());
                stmt.setString(2, signUpDTO.getUsername() + UUID.randomUUID());

                stmt.setString(3, signUpDTO.getPassword());

                stmt.setString(4, signUpDTO.getEmail());
                stmt.setString(5, RoleType.USER.name());
                stmt.setString(6, signUpDTO.getPhone());
                stmt.setString(7, signUpDTO.getAddress());

                LOGGER.info("Executing query: " + stmt.toString());
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    connection.commit();
                    LOGGER.info("User saved successfully");
                    return true;
                } else {
                    connection.rollback();
                    LOGGER.info("User not saved");
                    return false;
                }
            } catch (SQLException e) {
                connection.rollback();
                LOGGER.error("Error saving user, rolling back transaction", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            LOGGER.error("Database error", e);
        }

        return false;
    }

    @Override
    public boolean checkUserExists(String email) {
        LOGGER.info("Checking if user exists: " + email);
        String query = "SELECT email FROM users WHERE email = ?";
        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            LOGGER.info("Executing query: " + stmt.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LOGGER.info("User exists");
                return true;
            }
            LOGGER.info("User does not exist");
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return false;
    }
}
