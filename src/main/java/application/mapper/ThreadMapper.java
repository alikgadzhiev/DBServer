package application.mapper;

import application.models.Thread;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

import static application.configuration.TimeStampParser.fromTimestamp;

public class ThreadMapper implements RowMapper<Thread> {

    @Override
    public Thread mapRow(ResultSet resultSet, int i) throws SQLException {
        String author = resultSet.getString("author");
        String title = resultSet.getString("title");
        String forum = resultSet.getString("forum");
        String created = fromTimestamp(resultSet.getTimestamp("created"));
        String slug = resultSet.getString("slug");
        String message = resultSet.getString("message");
        int votes = resultSet.getInt("votes");
        int id = resultSet.getInt("id");
        return new Thread(id, title, author, forum, message, votes, slug, created);
    }

}
