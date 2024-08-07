package application.dao;

import application.mapper.UserMapper;
import application.models.Forum;
import application.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;

@Repository
public class UsersDao {

    private final JdbcTemplate jdbcTemplate;
    private final UsersHistoryDao usersHistoryDao;
    private final StatusDao statusDao;

    @Autowired
    public UsersDao(JdbcTemplate jdbcTemplate, UsersHistoryDao usersHistoryDao, StatusDao statusDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.usersHistoryDao = usersHistoryDao;
        this.statusDao = statusDao;
    }

    public boolean emailExists(String email) {
        try {
            User user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?",
                    new Object[]{email}, new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }

    public User getUser(String nickname) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE LOWER(nickname)=LOWER(?)",
                new UserMapper(), nickname);
    }

    public void createUser(User user) {
        jdbcTemplate.update("INSERT INTO users VALUES(?,?,?,?)",
                user.getNickname(), user.getFullname(), user.getAbout(), user.getEmail());
        usersHistoryDao.addUser(user.getNickname());
    }
    public ArrayList<User> getUsers(String slug, int limit, String since, boolean desc) {
        String sql = "SELECT u.* FROM users u WHERE u.nickname IN (SELECT fu.nickname FROM forum_users fu WHERE LOWER(fu.forum)=LOWER(?)) ";
        if(!since.isEmpty()){
            sql += "AND LOWER(u.nickname COLLATE \"ucs_basic\")";
            if(desc){
                sql += "<LOWER(? COLLATE \"ucs_basic\") ";
            } else {
                sql += ">LOWER(? COLLATE \"ucs_basic\") ";
            }
        }
        if (desc) {
            sql += "ORDER BY LOWER(u.nickname COLLATE \"ucs_basic\") DESC ";
        } else
            sql += "ORDER BY LOWER(u.nickname COLLATE \"ucs_basic\") ";
        sql += "LIMIT " + limit;

        if(since.isEmpty())
            return (ArrayList<User>) jdbcTemplate.query(sql, new UserMapper(), slug);
        else
            return (ArrayList<User>) jdbcTemplate.query(sql, new UserMapper(), slug, since);
    }

    public int getCount(){
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
    }

    public void updateUser(User user) {
        jdbcTemplate.update("UPDATE users SET fullname=?, about=?, email=? WHERE LOWER(nickname)=LOWER(?)",
                user.getFullname(), user.getAbout(), user.getEmail(), user.getNickname());
        usersHistoryDao.addUser(user.getNickname());
    }

    public void deleteAllUsers() {
        jdbcTemplate.execute("TRUNCATE TABLE users");
    }
}


