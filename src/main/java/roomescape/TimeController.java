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
@RequestMapping("/times")
public class TimeController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TimeController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping
    public ResponseEntity<Void> createTime(@RequestBody TimeRequest timeRequest) {
        if (timeRequest.getTime() == null || timeRequest.getTime().isEmpty()) {
            throw new InvalidReservationException("Invalid time details");
        }

        String sql = "INSERT INTO time (time) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, timeRequest.getTime());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        URI location = URI.create("/times/" + key.longValue());
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<List<Time>> readTimes() {
        String sql = "SELECT id, time FROM time";
        List<Time> times = jdbcTemplate.query(sql, new TimeRowMapper());
        return ResponseEntity.ok().body(times);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable long id) {
        String sql = "DELETE FROM time WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);

        if (rowsAffected > 0) {
            return ResponseEntity.noContent().build();
        } else {
            throw new NotFoundException("Time not found");
        }
    }

    // RowMapper for Time
    private static class TimeRowMapper implements RowMapper<Time> {
        @Override
        public Time mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Time(
                    rs.getLong("id"),
                    rs.getString("time")
            );
        }
    }
}
