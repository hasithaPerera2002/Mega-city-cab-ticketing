package org.icbt.hasitha.megacity.repository;

import org.icbt.hasitha.megacity.dto.TripDetailsDTO;
import org.icbt.hasitha.megacity.entity.User;
import org.icbt.hasitha.megacity.util.db.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class BookingRepo {

    private static Logger LOGGER = LoggerFactory.getLogger(BookingRepo.class);
    private UserRepo userRepo;
    private VehicleRepo vehicleRepo;

    public BookingRepo() {
        this.userRepo = new UserRepo();
        this.vehicleRepo = new VehicleRepo();
    }

    public boolean saveBooking(TripDetailsDTO trip) {
        LOGGER.info("Booking save start");
        User user;
        UUID vehicleId;
        try {
         user =   this.userRepo.findUserByEmail(trip.getCustomerEmail());
            vehicleId= this.vehicleRepo.getVehicleIdByType(trip.getVehicleType());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String  query = "INSERT INTO trips (trip_id, vehicle_id, customer_id, date, destination, start_time, fare, distance, passenger_count, start_location) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, UUID.randomUUID().toString());
                stmt.setString(2, vehicleId.toString());
                stmt.setString(3, user.getUser_id().toString());
                stmt.setDate(4, new java.sql.Date(trip.getDate().getTime()));
                stmt.setString(5, trip.getDestination());
                stmt.setString(6, trip.getStartTime());
                stmt.setString(7, trip.getFare());
                stmt.setString(8, trip.getDistance());
                stmt.setInt(9, trip.getPassengerCount());
                stmt.setString(10, trip.getStartLocation());
                int i = stmt.executeUpdate();
                conn.commit();
                LOGGER.info("Booking saved successfully");
                return i > 0;
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
