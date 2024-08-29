package roomescape;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ReservationDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReservationDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAll() {
        String sql = "SELECT r.id AS reservation_id, r.name, r.date, t.id AS time_id, t.time AS time_value " +
                "FROM reservation AS r INNER JOIN time AS t ON r.time_id = t.id";
        return jdbcTemplate.query(sql, new ReservationRowMapper());
    }

    public void save(ReservationRequest reservationRequest) {
        String sql = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, reservationRequest.getName(), reservationRequest.getDate(), reservationRequest.getTimeId());
    }

    public int deleteById(long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
        return 0;
    }

    private static class ReservationRowMapper implements RowMapper<Reservation> {
        @Override
        public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
            Time time = new Time(rs.getLong("time_id"), rs.getString("time_value"));
            return new Reservation(rs.getLong("reservation_id"), rs.getString("name"), rs.getString("date"), time);
        }
    }
}
