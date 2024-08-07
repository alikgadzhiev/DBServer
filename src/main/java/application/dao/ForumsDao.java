package application.dao;

import application.mapper.ForumMapper;
import application.models.Forum;
import application.models.Status;
import application.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class ForumsDao {

    private final JdbcTemplate jdbcTemplate;
    private final ThreadsDao threadsDao;
    private final UsersDao usersDao;

    @Autowired
    public ForumsDao(JdbcTemplate jdbcTemplate, ThreadsDao threadsDao, UsersDao usersDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.threadsDao = threadsDao;
        this.usersDao = usersDao;
    }

    public Forum getForum(String slug) {
        return jdbcTemplate.queryForObject("SELECT * FROM forums WHERE LOWER(slug)=?",
                new ForumMapper(), slug.toLowerCase());
    }

    public void updateCount(String slug, int addThreads, int addPosts) {
        jdbcTemplate.update("UPDATE forums SET posts=posts+?, threads=threads+? WHERE LOWER(slug)=?",
                addPosts, addThreads, slug.toLowerCase());
    }

    public int getCount(){
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM forums", Integer.class);
    }

    public void createForum(Forum forum) {
        jdbcTemplate.update("INSERT INTO forums VALUES(?,?,?,?,?)",
                forum.getTitle(), forum.getUser(), forum.getSlug(), forum.getPosts(), forum.getThreads());
        Status.total++;
    }

    public void deleteAllForums() {
        jdbcTemplate.execute("TRUNCATE TABLE forums");
    }
}
