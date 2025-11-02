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

    // Mapeja el ResultSet
    private static final class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setDescription(rs.getString("description"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            
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

    // Crea un nou usuari
    public String create(User user) {
        String sql = "INSERT INTO users (name, description, email, password) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
        user.getName(),
        user.getDescription(),
        user.getEmail(),
        user.getPassword());
        return "S’ha inserit l’usuari correctament.";
    }

    // Retorna tots els usuaris
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    // Troba un usuari per id o retorna null si no existeix
    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), id);
        return users.isEmpty() ? null : users.get(0);
    }
    // Actualitza totes les dades d'un usuari i posa data_updated actual
    public String update(Long id, User user) {
        String sql = "UPDATE users SET name = ?, description = ?, email = ?, password = ?, data_updated = CURRENT_TIMESTAMP WHERE id = ?";
        int result = jdbcTemplate.update(sql,
            user.getName(),
            user.getDescription(),
            user.getEmail(),
            user.getPassword(),
            id);
        return result > 0 ? "Usuari actualitzat correctament." : "No s'ha trobat l'usuari.";
    }

    // Actualitza nomes el nom i la data d'actualitzacio
    public String updateName(Long id, String name) {
        String sql = "UPDATE users SET name = ?, data_updated = CURRENT_TIMESTAMP WHERE id = ?";
        int result = jdbcTemplate.update(sql, name, id);
        return result > 0 ? "Nom d'usuari actualitzat correctament." : "No s'ha trobat l'usuari.";
    }

    // Elimina un usuari per id
    public String delete(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        int result = jdbcTemplate.update(sql, id);
        return result > 0 ? "Usuari eliminat correctament." : "No s'ha trobat l'usuari.";
    }
}