package org.icbt.hasitha.megacity.service.interfaces;

import org.icbt.hasitha.megacity.dto.ResultDTO;
import org.icbt.hasitha.megacity.dto.TripDetailsDTO;

import java.util.List;
import java.util.UUID;

public interface IBookingService extends ISuperService {
    ResultDTO<Boolean> save(TripDetailsDTO t);

    ResultDTO<Boolean> updateBooking(TripDetailsDTO t);

    ResultDTO<Boolean> deleteBooking(UUID t);

    List<TripDetailsDTO> getAllBookings(String t);
}
