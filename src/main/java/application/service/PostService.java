package application.service;

import application.exceptions.NotFoundException;
import application.models.Forum;
import application.models.FullInfo;
import application.models.Post;
import application.models.PostUpdate;
import application.dao.ForumsDao;
import application.dao.PostsDao;
import application.dao.ThreadsDao;
import application.dao.UsersDao;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;

@Service
public class PostService {

    private final PostsDao postsDao;
    private final ForumsDao forumsDao;
    private final UsersDao usersDao;
    private final ThreadsDao threadsDao;

    @Autowired
    public PostService(PostsDao postsDao, ForumsDao forumsDao, UsersDao usersDao, ThreadsDao threadsDao) {
        this.postsDao = postsDao;
        this.forumsDao = forumsDao;
        this.usersDao = usersDao;
        this.threadsDao = threadsDao;
    }

    public Post updatePost(long id, Post post){
        try {
            Post found = postsDao.getPost(id);
            found.setCreated("1970-01-01T00:00:00.000Z");
            post.checkNull(found);
            boolean edited = (!found.getMessage().equals(post.getMessage()) && !post.getMessage().equals(""));
            postsDao.updatePost(id, post, edited);
            if(edited) {
                found.setMessage(post.getMessage());
                found.setEdited(true);
            }
            return found;
        } catch (EmptyResultDataAccessException e){
            throw new NotFoundException("Can't find post by id: " + id, e);
        }
    }

    public FullInfo getInfo(long id, ArrayList<String> related){
        try {
            Post post = postsDao.getPost(id);
            post.setCreated("1970-01-01T00:00:00.000Z");
            FullInfo list = new FullInfo();
            list.setPost(post);
            try {
                for (String object : related) {
                    if (object.equals("user")) {
                        list.setAuthor(usersDao.getUser(post.getAuthor()));
                    } else if (object.equals("forum")) {
                        list.setForum(forumsDao.getForum(post.getForum()));
                    } else if (object.equals("thread")) {
                        list.setThread(threadsDao.getThreadById(post.getThread()));
                    }
                }
                return list;
            } catch (NotFoundException e) {
                throw new NotFoundException("Can't find thread by id: " + post.getThread(), e);
            } catch (NullPointerException e) {
                return list;
            }
        } catch (EmptyResultDataAccessException e){
            throw new NotFoundException("Can't find post with id: " + id, e);
        }
    }
}
