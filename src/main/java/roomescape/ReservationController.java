package roomescape;

import roomescape.exception.InvalidReservationException;
import roomescape.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class ReservationController {

    private final List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";
    }

    @GetMapping("/reservations")
    @ResponseBody
    public ResponseEntity<List<Reservation>> readReservations() {
        return ResponseEntity.ok().body(reservations);
    }

    @PostMapping("/reservations")
    @ResponseBody
    public ResponseEntity<Void> createReservation(@RequestBody Reservation reservation) {
        if (reservation.getName() == null || reservation.getName().isEmpty() ||
                reservation.getDate() == null || reservation.getDate().isEmpty() ||
                reservation.getTime() == null || reservation.getTime().isEmpty()) {
            throw new InvalidReservationException("Invalid reservation details");
        }

        Reservation newReservation = Reservation.toEntity(reservation, index.getAndIncrement());
        reservations.add(newReservation);
        return ResponseEntity.created(URI.create("/reservations/" + newReservation.getId())).build();
    }

    @DeleteMapping("/reservations/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteReservation(@PathVariable long id) {
        Optional<Reservation> reservation = reservations.stream()
                .filter(r -> r.getId() == id)
                .findFirst();

        if (reservation.isPresent()) {
            reservations.remove(reservation.get());
            return ResponseEntity.noContent().build();
        } else {
            throw new NotFoundException("Reservation not found");
        }
    }
}
