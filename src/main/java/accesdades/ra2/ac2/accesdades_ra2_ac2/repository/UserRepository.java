package  accesdades.ra2.ac2.accesdades_ra2_ac2.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import accesdades.ra2.ac2.accesdades_ra2_ac2.model.User;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setDescription(rs.getString("description"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            
            // NOT NULL
            Timestamp ultimAcces = rs.getTimestamp("ultim_acces");
            if (ultimAcces != null) {
                user.setUltimAcces(ultimAcces.toLocalDateTime());
            }
            
            Timestamp dataCreated = rs.getTimestamp("data_created");
            if (dataCreated != null) {
                user.setDataCreated(dataCreated.toLocalDateTime());
            }
            
            Timestamp dataUpdated = rs.getTimestamp("data_updated");
            if (dataUpdated != null) {
                user.setDataUpdated(dataUpdated.toLocalDateTime());
            }
            
            return user;
        }
    }

    public String create(User user) {
        String sql = "INSERT INTO users (name, description, email, password) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
        user.getName(),
        user.getDescription(),
        user.getEmail(),
        user.getPassword());
        return "S’ha inserit l’usuari correctament.";
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), id);
        return users.isEmpty() ? null : users.get(0);
    }
}