package application.mapper;

import application.configuration.TimeStampParser;
import application.models.Post;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;


public class PostMapper implements RowMapper<Post>{
    @Override
    public Post mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("id");
        long parent = resultSet.getLong("parent");
        String author = resultSet.getString("author");
        String message = resultSet.getString("message");
        boolean isEdited = resultSet.getBoolean("is_edited");
        String forum = resultSet.getString("forum");
        int thread = resultSet.getInt("thread");
        String created = TimeStampParser.fromTimestamp(resultSet.getTimestamp("created"));
        return new Post(id, parent, author, message, isEdited, forum, thread, created);
    }
}
