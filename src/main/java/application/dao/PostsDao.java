package application.dao;

import application.configuration.TimeStampParser;
import application.mapper.PostMapper;
import application.mapper.ThreadMapper;
import application.models.Post;
import application.models.PostUpdate;
import application.models.Status;
import application.models.Thread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

@Repository
public class PostsDao {

    private final JdbcTemplate jdbcTemplate;
    private final StatusDao statusDao;

    @Autowired
    public PostsDao(JdbcTemplate jdbcTemplate, StatusDao statusDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.statusDao = statusDao;
    }

    public void createPost(Post post) {
        if(post.getCreated() == null){
            post.setCreated("1970-01-01T00:00:00.000Z");
        }
//        long sum = statusDao.sum();
        long sum = Status.total;
        String path = getPath(post.getParent());
        jdbcTemplate.update("INSERT INTO posts VALUES(?,?,?,?,?,?,?,?,?)",
                sum, post.getParent(), post.getAuthor(), post.getMessage(), post.isEdited(), post.getForum(),
                post.getThread(), TimeStampParser.toTimeStamp(post.getCreated()), path + "." + sum);
//        try {
//            jdbcTemplate.update("INSERT INTO forum_users VALUES(?,?)",
//                    post.getForum(), post.getAuthor());
//        } catch (DuplicateKeyException e){}
        Status.total++;
//        statusDao.updateStatus(0, 0, 0, 1);
        post.setId(sum);
    }

    public String getPath(long parent){
        if(parent == 0)
            return "";
        return jdbcTemplate.queryForObject("SELECT path FROM posts WHERE id=?", String.class, parent);
    }

    public Post getPost(long id) {
            return jdbcTemplate.queryForObject("SELECT * FROM posts WHERE id=?",
                    new PostMapper(), id);
    }

    public long getCount(){
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM posts", Long.class);
    }

    public boolean isParentAbsent(long parent) {
        try {
            Post post = jdbcTemplate.queryForObject("SELECT * FROM posts WHERE id=?",
                    new PostMapper(), parent);
            return false;
        } catch (EmptyResultDataAccessException e) {
            return true;
        }
    }

    public void updatePost(long id, Post post, boolean edited) {
        if(Objects.equals(post.getMessage(), ""))
            return;
        jdbcTemplate.update("UPDATE posts SET message=?, is_edited=? WHERE id=?",
                post.getMessage(), edited, id);
    }

    public ArrayList<Integer> getParents(int threadId, boolean desc, int limit, long since) {
        String sql = "SELECT p.id FROM posts p WHERE p.parent=0 AND p.thread=? ";
        if(since != 0){
            sql += "AND p.path";
            if(desc){
                sql += "<? ";
            } else {
                sql += ">? ";
            }
            sql += "AND NOT (? LIKE CONCAT('%', p.path, '%')) ";
        }
        sql += "ORDER BY p.id " + (desc ? "DESC" : "ASC");
        sql += " LIMIT " + limit;
        if(since != 0){
            return (ArrayList<Integer>) jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("id"), threadId, getPath(since), getPath(since));
        } else {
            return (ArrayList<Integer>) jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("id"), threadId);
        }
    }

    public ArrayList<Post> getParents(ArrayList<Integer> parentIds) {
        try {
            return (ArrayList<Post>) jdbcTemplate.query("SELECT * FROM posts WHERE id IN(?)",
                    new PostMapper(), parentIds);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public ArrayList<Post> flatSort(int threadId, int limit, long since, boolean desc) {
        String sql = "SELECT * FROM posts WHERE thread=? ";
        if(since != 0){
            sql += "AND id";
            if (!desc) {
                sql += ">? ";
            } else {
                sql += "<? ";
            }
        }
        if (!desc) {
            sql += "ORDER BY created ASC, ";
        } else {
            sql += "ORDER BY created DESC, ";
        }
        sql += "id " + (desc ? "DESC" : "ASC");
        sql += " LIMIT " + limit;
        if(since != 0) {
            return (ArrayList<Post>) jdbcTemplate.query(sql, new PostMapper(), threadId, since);
        } else {
            return (ArrayList<Post>) jdbcTemplate.query(sql, new PostMapper(), threadId);
        }
    }

    public ArrayList<Post> treeSort(int threadId, int limit, long since, boolean desc){
        String sql = "SELECT p.* FROM posts p WHERE p.thread=? ";
        if(since != 0){
            sql += "AND p.path";
            if(desc){
                sql += "<? ";
            } else {
                sql += ">? ";
            }
        }
        sql += "ORDER BY p.path " + (desc ? "DESC" : "ASC");
        sql += " LIMIT " + limit;
        if(since != 0) {
            return (ArrayList<Post>) jdbcTemplate.query(sql, new PostMapper(), threadId, getPath(since));
        } else {
            return (ArrayList<Post>) jdbcTemplate.query(sql, new PostMapper(), threadId);
        }
    }

    public ArrayList<Post> parentTreeSort(int threadId, int limit, long since, boolean desc) {
        ArrayList<Integer> parentIds = getParents(threadId, desc, limit, since);
        String sql = "SELECT p.* FROM posts p WHERE p.path LIKE ? AND p.thread=? ";
        sql += " ORDER BY p.path ASC" + ", id " + (desc ? "DESC" : "ASC");
        ArrayList<Post> posts = new ArrayList<>();
        for (Integer parent : parentIds) {
            posts.addAll(jdbcTemplate.query(sql, new PostMapper(), "%" + Integer.toString(parent) + "%", threadId));
        }

        return posts;
    }

    public void deleteAllPosts() {
        jdbcTemplate.execute("TRUNCATE TABLE posts");
    }
}
