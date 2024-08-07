package application.mapper;

import application.models.Vote;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VoteMapper implements RowMapper<Vote> {
    @Override
    public Vote mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        String nickname = resultSet.getString("nickname");
        int voice = resultSet.getInt("voice");
        return new Vote(nickname, voice);
    }
}
