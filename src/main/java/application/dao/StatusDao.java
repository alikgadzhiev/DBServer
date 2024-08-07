package application.dao;

import application.models.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class StatusDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StatusDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void updateStatus(int users, int forums, int threads, long posts){
        if(getStatus() == null)
            jdbcTemplate.update("INSERT INTO status VALUES(0,0,0,0)");
        jdbcTemplate.update("UPDATE status SET users=users+?, forums=forums+?, threads=threads+?, posts=posts+?",
                users, forums, threads, posts);
    }

    public void clearData(){
        jdbcTemplate.update("TRUNCATE TABLE status");
    }

    public long sum(){
        Status status = getStatus();
        return status.sumOfStatus();
    }

    public Status getStatus(){
        ArrayList<Status> status = (ArrayList<Status>) jdbcTemplate.query("SELECT * FROM status", BeanPropertyRowMapper.newInstance(Status.class));
        if(status.isEmpty())
            return null;
        else return status.get(0);
    }
}
