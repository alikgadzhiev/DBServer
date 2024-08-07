package application.mapper;

import application.models.Forum;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ForumMapper implements RowMapper<Forum>{
    @Override
    public Forum mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        String title = resultSet.getString("title");
        String user = resultSet.getString("user");
        String slug = resultSet.getString("slug");
        long posts = resultSet.getLong("posts");
        int threads = resultSet.getInt("threads");
        return new Forum(title, user, slug, posts, threads);
    }
}
