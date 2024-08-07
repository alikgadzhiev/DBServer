package application.dao;

import application.mapper.UserMapper;
import application.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class UsersHistoryDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UsersHistoryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ArrayList<User> getUsers(String nickname, String email) {
        try {
            return (ArrayList<User>) jdbcTemplate.query("SELECT * FROM users_history WHERE LOWER(nickname)=LOWER(?) OR LOWER(email)=LOWER(?)",
                    new UserMapper(), nickname, email);
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    public void addUser(String nickname){
        jdbcTemplate.update("INSERT INTO users_history SELECT * FROM users WHERE nickname=?",
                nickname);
    }

    public void deleteAllData(){
        jdbcTemplate.execute("TRUNCATE TABLE users_history");
    }
}
