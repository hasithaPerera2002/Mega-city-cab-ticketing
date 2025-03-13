package org.icbt.hasitha.megacity.repository.interfaces;

import org.icbt.hasitha.megacity.dto.TripDetailsDTO;
import org.icbt.hasitha.megacity.entity.Trip;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface IBookingRepo extends ISuperRepo{
    boolean saveBooking(TripDetailsDTO trip);
    List<Trip> getTrips(String email) throws SQLException;
    boolean updateBooking(TripDetailsDTO trip);
    boolean deleteBooking(UUID tripId) throws SQLException;
}
