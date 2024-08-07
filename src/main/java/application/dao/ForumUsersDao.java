package application.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ForumUsersDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ForumUsersDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void deleteAllData(){
        jdbcTemplate.update("TRUNCATE TABLE forum_users");
    }
}
