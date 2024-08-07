package application.dao;

import application.configuration.TimeStampParser;
import application.mapper.ThreadMapper;
import application.models.Status;
import application.models.Thread;
import application.models.ThreadUpdate;
import application.models.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;

@Repository
public class ThreadsDao {

    private final JdbcTemplate jdbcTemplate;
    private final StatusDao statusDao;

    @Autowired
    public ThreadsDao(JdbcTemplate jdbcTemplate, StatusDao statusDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.statusDao = statusDao;
    }

    public Thread getThreadById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM threads WHERE id=?",
                new ThreadMapper(), id);
    }

    public Thread getThreadByForum(String forum) {
        return jdbcTemplate.queryForObject("SELECT * FROM threads WHERE forum=?",
                new ThreadMapper(), forum);
    }

    public Thread getThreadBySlug(String slug) {
        return jdbcTemplate.queryForObject("SELECT * FROM threads WHERE LOWER(slug)=LOWER(?)",
                new ThreadMapper(), slug);
    }

    public ArrayList<Thread> getThreads(String slugForum, int limit, String since, boolean desc) {
        String sql = "SELECT t.* FROM threads t WHERE LOWER(t.forum)=? AND t.created IS NOT NULL ";
        if (since != null) {
            if (!desc) {
                sql += "AND t.created >=? ";
            } else {
                sql += "AND t.created <=? ";
            }
        }
        if (!desc) {
            sql += "ORDER BY t.created ASC ";
        } else {
            sql += "ORDER BY t.created DESC ";
        }
        sql += "LIMIT " + limit;
        if (since != null) {
            return (ArrayList<Thread>) jdbcTemplate.query(sql, new ThreadMapper(), slugForum.toLowerCase(), TimeStampParser.toTimeStamp(since));
        } else {
            return (ArrayList<Thread>) jdbcTemplate.query(sql, new ThreadMapper(), slugForum.toLowerCase());
        }
    }

    public Thread getThreadBySlugAndId(String slugForum, long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM threads WHERE forum=? AND id=?",
                    new Object[]{slugForum, id}, new ThreadMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int getCount(){
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM threads", Integer.class);
    }

    public void createThread(Thread thread) {
        jdbcTemplate.update("INSERT INTO threads VALUES(?,?,?,?,?,?,?,?)",
                Status.total, thread.getTitle(), thread.getAuthor(), thread.getForum(),
                thread.getMessage(), thread.getVotes(), thread.getSlug(), TimeStampParser.toTimeStamp(thread.getCreated()));
        jdbcTemplate.update("UPDATE forums SET posts=posts+?, threads=threads+? WHERE LOWER(slug)=LOWER(?)",
                0, 1, thread.getForum());
        try {
            jdbcTemplate.update("INSERT INTO forum_users VALUES(?,?)",
                    thread.getForum(), thread.getAuthor());
        } catch (DuplicateKeyException e){}
    }

    public void voteForThread(int id, int votes){
        String sql = "UPDATE threads SET votes=? WHERE id=?";
        jdbcTemplate.update(sql, votes, id);
    }

    public void updateThread(int id, Thread thread) {
        jdbcTemplate.update("UPDATE threads SET title=?, message=? WHERE id=?",
                thread.getTitle(), thread.getMessage(), id);
    }

    public void deleteAllThreads() {
        jdbcTemplate.update("TRUNCATE TABLE threads");
    }
}
