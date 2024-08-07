package application.service;

import application.dao.StatusDao;
import application.models.Forum;
import application.models.Thread;
import application.models.User;
import application.dao.ForumsDao;
import application.dao.ThreadsDao;
import application.dao.UsersDao;
import application.exceptions.DuplicateException;
import application.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ForumService {

    private final UsersDao usersDao;
    private final ForumsDao forumsDao;
    private final ThreadsDao threadsDao;
    private final StatusDao statusDao;


    @Autowired
    public ForumService(UsersDao usersDao, ForumsDao forumsDao, ThreadsDao threadsDao, StatusDao statusDao) {
        this.usersDao = usersDao;
        this.forumsDao = forumsDao;
        this.threadsDao = threadsDao;
        this.statusDao = statusDao;
    }

    public Forum createForum(Forum forum){
        try {
            forum.checkNull();
//            usersDao.getUser(forum.getUser());
//            forum.setUser(user.getNickname());
            forumsDao.createForum(forum);
//            statusDao.updateStatus(0, 1, 0, 0);
            return forum;
        } catch (EmptyResultDataAccessException e){
            throw new NotFoundException("Can't find user with nickname: " + forum.getUser(), e);
        } catch(DuplicateKeyException e){
            throw new DuplicateException("Forum already exist", e);
        }
    }

    public List<Thread> getThreads(String slugForum, int limit, String since, boolean desc){
        try {
            forumsDao.getForum(slugForum);
            return threadsDao.getThreads(slugForum, limit, since, desc);
        } catch (EmptyResultDataAccessException e){
            throw new NotFoundException("Can't find forum by slug: " + slugForum, e);
        }
    }

    public ArrayList<User> getUsers(String slug, int limit, String since, boolean desc){
        try {
            forumsDao.getForum(slug);
            return usersDao.getUsers(slug, limit, since, desc);
        } catch (EmptyResultDataAccessException e){
            throw new NotFoundException("Can't find forum by slug: " + slug, e);
        }
    }

    public Forum getForum(String slug){
        try {
            return forumsDao.getForum(slug);
        } catch(EmptyResultDataAccessException e){
            throw new NotFoundException("Can't find forum with slug: " + slug, e);
        }
    }
}
