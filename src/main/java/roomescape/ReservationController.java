package roomescape;

import roomescape.exception.InvalidReservationException;
import roomescape.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Controller
public class ReservationController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReservationController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";
    }

    @GetMapping("/reservations")
    @ResponseBody
    public ResponseEntity<List<Reservation>> readReservations() {
        String sql = "SELECT id, name, date, time FROM reservation";
        List<Reservation> reservations = jdbcTemplate.query(sql, new ReservationRowMapper());
        return ResponseEntity.ok().body(reservations);
    }

//    @PostMapping("/reservations")
//    @ResponseBody
//    public ResponseEntity<Void> createReservation(@RequestBody Reservation reservation) {
//        if (reservation.getName() == null || reservation.getName().isEmpty() ||
//                reservation.getDate() == null || reservation.getDate().isEmpty() ||
//                reservation.getTime() == null || reservation.getTime().isEmpty()) {
//            throw new InvalidReservationException("Invalid reservation details");
//        }
//
//        Reservation newReservation = Reservation.toEntity(reservation, index.getAndIncrement());
//        reservations.add(newReservation);
//        return ResponseEntity.created(URI.create("/reservations/" + newReservation.getId())).build();
//    }
//
//    @DeleteMapping("/reservations/{id}")
//    @ResponseBody
//    public ResponseEntity<Void> deleteReservation(@PathVariable long id) {
//        Optional<Reservation> reservation = reservations.stream()
//                .filter(r -> r.getId() == id)
//                .findFirst();
//
//        if (reservation.isPresent()) {
//            reservations.remove(reservation.get());
//            return ResponseEntity.noContent().build();
//        } else {
//            throw new NotFoundException("Reservation not found");
//        }
//    }

    // RowMapper 이용하기
    private static class ReservationRowMapper implements RowMapper<Reservation> {
        @Override
        public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Reservation(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("date"),
                    rs.getString("time")
            );
        }
    }
}
