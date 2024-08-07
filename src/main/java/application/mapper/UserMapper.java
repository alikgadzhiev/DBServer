package application.mapper;

import application.models.User;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User>{

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        String nickname = resultSet.getString("nickname");
        String fullname = resultSet.getString("fullname");
        String about = resultSet.getString("about");
        String email = resultSet.getString("email");
        return new User(nickname, fullname, about, email);
    }
}
