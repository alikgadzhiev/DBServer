package application.dao;

import application.mapper.VoteMapper;
import application.models.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class VotesDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public VotesDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addVote(Vote vote, int id){
        try {
            int voice = jdbcTemplate.queryForObject("SELECT voice FROM votes WHERE LOWER(nickname)=LOWER(?) AND thread=?",
                    Integer.class, vote.getNickname(), id);
            if(voice == vote.getVoice())
                vote.setVoice(0);
            else {
                jdbcTemplate.update("UPDATE votes SET voice=? WHERE LOWER(nickname)=LOWER(?) AND thread=?",
                        vote.getVoice(), vote.getNickname(), id);
                vote.setVoice(vote.getVoice() * 2);
            }
        } catch (EmptyResultDataAccessException e){
            jdbcTemplate.update("INSERT INTO votes VALUES(?,?,?)",
                                vote.getNickname(), id, vote.getVoice());
        }
    }

    public void deleteAllVotes(){
        jdbcTemplate.update("TRUNCATE TABLE votes");
    }
}
