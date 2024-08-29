package roomescape;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;
import roomescape.exception.InvalidReservationException;
import roomescape.exception.NotFoundException;

import java.net.URI;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReservationController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/reservations")
    @ResponseBody
    public ResponseEntity<List<Reservation>> readReservations() {
        String sql = "SELECT r.id AS reservation_id, r.name, r.date, t.id AS time_id, t.time AS time_value FROM reservation AS r INNER JOIN time AS t ON r.time_id = t.id";
        List<Reservation> reservations = jdbcTemplate.query(sql, new ReservationRowMapper());
        return ResponseEntity.ok().body(reservations);
    }


    @PostMapping
    public ResponseEntity<Void> createReservation(@RequestBody ReservationRequest reservationRequest) {
        if (reservationRequest.getName() == null || reservationRequest.getName().isEmpty() ||
                reservationRequest.getDate() == null || reservationRequest.getDate().isEmpty() ||
                reservationRequest.getTimeId() == null) {
            throw new InvalidReservationException("Invalid reservation details");
        }

        String sql = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, reservationRequest.getName());
            ps.setString(2, reservationRequest.getDate());
            ps.setLong(3, reservationRequest.getTimeId());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        URI location = URI.create("/reservations/" + key.longValue());
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);

        if (rowsAffected > 0) {
            return ResponseEntity.noContent().build();
        } else {
            throw new NotFoundException("Reservation not found");
        }
    }

    // RowMapper for Reservation
    private static class ReservationRowMapper implements RowMapper<Reservation> {
        @Override
        public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
            Time time = new Time(
                    rs.getLong("time_id"),
                    rs.getString("time_value")
            );

            return new Reservation(
                    rs.getLong("reservation_id"),
                    rs.getString("name"),
                    rs.getString("date"),
                    time
            );
        }
    }

}
