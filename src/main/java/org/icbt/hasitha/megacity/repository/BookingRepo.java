package org.icbt.hasitha.megacity.repository;

import org.icbt.hasitha.megacity.dto.TripDetailsDTO;
import org.icbt.hasitha.megacity.entity.Trip;
import org.icbt.hasitha.megacity.entity.User;
import org.icbt.hasitha.megacity.util.db.DBConnection;
import org.icbt.hasitha.megacity.util.enums.VehicleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookingRepo {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingRepo.class);
    private final UserRepo userRepo = new UserRepo();
    private final VehicleRepo vehicleRepo = new VehicleRepo();



    public boolean saveBooking(TripDetailsDTO trip) {
        LOGGER.info("Booking save start");
        User user;
        UUID vehicleId;
        try {
         user =   this.userRepo.findUserByEmail(trip.getCustomerEmail());
         LOGGER.info("User found");
            vehicleId= this.vehicleRepo.getVehicleIdByType(VehicleTypes.valueOf(trip.getVehicleType()));
            LOGGER.info("Vehicle found");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String  query = "INSERT INTO trips (trip_id, vehicle_id, customer_id, date, destination, starttime, fare, distance, passenger_count, starting_location) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            LOGGER.info(trip.toString());
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setObject(1, UUID.randomUUID());
                stmt.setObject(2, vehicleId);
                stmt.setObject(3, user.getUser_id());
                stmt.setDate(4, new java.sql.Date(trip.getDate()));
                stmt.setString(5, trip.getDestination());
                stmt.setString(6, trip.getStartTime());
                stmt.setDouble(7, Double.parseDouble(trip.getFare()));
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

    public List<Trip> getTrips(String email) throws SQLException {
        User user = this.userRepo.findUserByEmail(email);
        String query = "SELECT * FROM trips where customer_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection()){
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setObject(1, user.getUser_id());
                ResultSet rs = stmt.executeQuery();
                List<Trip> trips = new ArrayList<>();
                while (rs.next()) {
                    Trip trip = new Trip();
                    trip.setTripId(rs.getString("trip_id"));
                    trip.setVehicleId(rs.getString("vehicle_id"));
                    trip.setCustomerId(rs.getString("customer_id"));
                    trip.setDate(rs.getDate("date").getTime());
                    trip.setDestination(rs.getString("destination"));
                    trip.setStartTime(rs.getString("starttime"));
                    trip.setFare(String.valueOf(rs.getDouble("fare")));
                    trip.setDistance(rs.getString("distance"));
                    trip.setPassengerCount(rs.getInt("passenger_count"));
                    trip.setStartLocation(rs.getString("starting_location"));
                    trips.add(trip);
                }

                return trips;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateBooking(TripDetailsDTO trip) {
        LOGGER.info("Updating booking with ID: {}", trip.getTripId());

        String query = "UPDATE trips SET vehicle_id = ?, date = ?, destination = ?, starttime = ?, fare = ?, distance = ?, passenger_count = ?, starting_location = ? WHERE trip_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setObject(1, this.vehicleRepo.getVehicleIdByType(VehicleTypes.valueOf(trip.getVehicleType())));
                stmt.setDate(2, new java.sql.Date(trip.getDate()));
                stmt.setString(3, trip.getDestination());
                stmt.setString(4, trip.getStartTime());
                stmt.setDouble(5, Double.parseDouble(trip.getFare()));
                stmt.setString(6, trip.getDistance());
                stmt.setInt(7, trip.getPassengerCount());
                stmt.setString(8, trip.getStartLocation());
                stmt.setObject(9, UUID.fromString(trip.getTripId()));

                int updatedRows = stmt.executeUpdate();
                conn.commit();
                LOGGER.info("Booking updated successfully");
                return updatedRows > 0;
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteBooking(UUID tripId) throws SQLException,RuntimeException {
        LOGGER.info("Deleting booking with ID: {}", tripId);

        String query = "DELETE FROM trips WHERE trip_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setObject(1, tripId);
                System.out.println(stmt);
                int deletedRows = stmt.executeUpdate();
                LOGGER.info("Booking deleted successfully: {}", deletedRows);

                return deletedRows > 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
