package application.service;

import application.models.Status;
import application.models.User;
import application.dao.*;
import application.exceptions.DuplicateException;
import application.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UsersDao usersDao;
    private final UsersHistoryDao usersHistoryDao;
    private final StatusDao statusDao;


    @Autowired
    public UserService(UsersDao usersDao, UsersHistoryDao usersHistoryDao, StatusDao statusDao) {
        this.usersDao = usersDao;
        this.usersHistoryDao = usersHistoryDao;
        this.statusDao = statusDao;
    }

    public User createUser(String nickname, User user){
        try {
            user.setNickname(nickname);
            user.checkNull();
            usersDao.createUser(user);
            Status.total++;
//            statusDao.updateStatus(1, 0, 0, 0);
            return user;
        } catch (DuplicateKeyException e){
            throw new DuplicateException("User already exist", e);
        }
    }
    public User getUser(String nickname){
        try {
            return usersDao.getUser(nickname);
        } catch (EmptyResultDataAccessException e){
            throw new NotFoundException("Can't find user by nickname: " + nickname, e);
        }
    }

    public List<User> getUsers(String nickname, String email) {
        try {
            return usersHistoryDao.getUsers(nickname, email);
        } catch (EmptyResultDataAccessException e){
            throw new NotFoundException("User not found", e);
        }
    }

    public User updateUser(String nickname, User user){
        try {
            User found = usersDao.getUser(nickname);
            user.setNickname(nickname);
            user.checkNull(found);
            usersDao.updateUser(user);
            return getUser(nickname);
        } catch(EmptyResultDataAccessException e){
            throw new NotFoundException("Can't find user by nickname: " + nickname, e);
        } catch(DuplicateKeyException e){
            throw new DuplicateException("User already exist", e);
        }
    }
}
