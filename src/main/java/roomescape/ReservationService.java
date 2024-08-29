package roomescape;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.exception.InvalidReservationException;
import roomescape.exception.NotFoundException;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationDAO reservationDAO;

    @Autowired
    public ReservationService(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    public List<Reservation> getAllReservations() {
        return reservationDAO.findAll();
    }

    public void createReservation(ReservationRequest reservationRequest) {
        if (reservationRequest.getName() == null || reservationRequest.getName().isEmpty() ||
                reservationRequest.getDate() == null || reservationRequest.getDate().isEmpty() ||
                reservationRequest.getTimeId() == null) {
            throw new InvalidReservationException("Invalid reservation details");
        }
        reservationDAO.save(reservationRequest);
    }

    public void deleteReservation(long id) {
        int rowsAffected = reservationDAO.deleteById(id);
        if (rowsAffected == 0) {
            throw new NotFoundException("Reservation not found");
        }
    }
}
