package application.service;

import application.dao.*;
import application.models.Status;
import application.models.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StatusService {

    private final UsersDao usersDao;
    private final ForumsDao forumsDao;
    private final ThreadsDao threadsDao;
    private final PostsDao postsDao;
    private final UsersHistoryDao usersHistoryDao;
    private final VotesDao votesDao;
    private final ForumUsersDao forumUsersDao;
    private final StatusDao statusDao;

    @Autowired
    public StatusService(UsersDao usersDao, ForumsDao forumsDao, ThreadsDao threadsDao,
                         UsersHistoryDao usersHistoryDao, PostsDao postsDao, VotesDao votesDao, ForumUsersDao forumUsersDao, StatusDao statusDao) {
        this.usersDao = usersDao;
        this.forumsDao = forumsDao;
        this.threadsDao = threadsDao;
        this.usersHistoryDao = usersHistoryDao;
        this.postsDao = postsDao;
        this.votesDao = votesDao;
        this.forumUsersDao = forumUsersDao;
        this.statusDao = statusDao;
    }
    public void clearStatus(){
        usersDao.deleteAllUsers();
        forumsDao.deleteAllForums();
        threadsDao.deleteAllThreads();
        usersHistoryDao.deleteAllData();
        postsDao.deleteAllPosts();
        votesDao.deleteAllVotes();
        forumUsersDao.deleteAllData();
//        statusDao.clearData();
        Status.total = 0;
    }

    public Status getStatus(){
        return new Status(usersDao.getCount(), forumsDao.getCount(), threadsDao.getCount(), postsDao.getCount());
    }
}
