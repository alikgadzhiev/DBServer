package application.service;

import application.exceptions.DuplicateException;
import application.models.*;
import application.models.Thread;
import application.dao.*;
import application.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.module.FindException;
import java.util.ArrayList;
import java.util.HashSet;

@Service
public class ThreadService {

    private final UsersDao usersDao;
    private final ForumsDao forumsDao;
    private final ThreadsDao threadsDao;
    private final StatusDao statusDao;
    private final PostsDao postsDao;
    private final VotesDao votesDao;


    @Autowired
    public ThreadService(UsersDao usersDao, ForumsDao forumsDao, ThreadsDao threadsDao, StatusDao statusDao, PostsDao postsDao, VotesDao votesDao) {
        this.usersDao = usersDao;
        this.forumsDao = forumsDao;
        this.threadsDao = threadsDao;
        this.statusDao = statusDao;
        this.postsDao = postsDao;
        this.votesDao = votesDao;
    }

    public Thread createThread(String slugForum, Thread thread){
        try {
//            forumsDao.getForum(slugForum);
//            usersDao.getUser(thread.getAuthor());
//            thread.setForum(forum.getSlug());
//            thread.setAuthor(user.getNickname());
            thread.checkNull();
            threadsDao.createThread(thread);
            thread.setId((int) Status.total);
//            thread.setId((int) statusDao.sum());
//            statusDao.updateStatus(0, 0, 1, 0);
            Status.total++;
            return thread;
        } catch(DuplicateKeyException e){
            throw new DuplicateException("Thread already exist", e);
        } catch(EmptyResultDataAccessException e){
            throw new NotFoundException("Can't find user or forum", e);
        }
    }

    public ArrayList<Post> createPosts(String slug_or_id, ArrayList<Post> posts){
        try {
            Thread found = distinguish(slug_or_id);
//            HashSet<Long> parentIds = new HashSet<>();
//            for (Post post:
//                 posts) {
//                parentIds.add(post.getParent());
//            }
//            ArrayList<Post> parents
            for (Post post : posts) {
//                if(post.getParent() != 0 && Status.total < post.getParent()){
//                    throw new DuplicateException("Parent post was created in another thread", new EmptyResultDataAccessException(1));
//                }
//                try {
//                    if (post.getParent() != 0 && postsDao.getPost(post.getParent()).getThread() != found.getId()) {
//                        throw new DuplicateException("Parent post was created in another thread", new EmptyResultDataAccessException(1));
//                    }
//                } catch (EmptyResultDataAccessException e){
//                    throw new NotFoundException("Can't find post by id: " + post.getParent(), e);
//                }
//                try {
//                    usersDao.getUser(post.getAuthor());
//                } catch (EmptyResultDataAccessException e){
//                    throw new NotFoundException("Can't find post author by nickname: " + post.getAuthor(), new EmptyResultDataAccessException(1));
//                }
                post.setEdited(false);
                post.setForum(found.getForum());
                post.setThread(found.getId());
                postsDao.createPost(post);
            }
//            posts.forEach(postsDao::createPost);
//            forumsDao.updateCount(found.getForum(), 0, posts.size());
            return posts;
        } catch (EmptyResultDataAccessException e){
            throw new NotFoundException("Can't find thread by id: " + slug_or_id, e);
        }
    }

    public ArrayList<Post> getPosts(String slug_or_id, int limit, long since, String sort, boolean desc){
        try {
            Thread found = distinguish(slug_or_id);
            ArrayList<Post> posts = null;
            if(sort.equals("flat")) {
                posts = postsDao.flatSort(found.getId(), limit, since, desc);
            } else if(sort.equals("tree")){
                posts = postsDao.treeSort(found.getId(), limit, since, desc);
            } else if(sort.equals("parent_tree")){
                posts = postsDao.parentTreeSort(found.getId(), limit, since, desc);
            }
            return posts;
        } catch (EmptyResultDataAccessException e){
            throw new NotFoundException("Can't find thread by id: " + slug_or_id, e);
        }
    }

    public Thread getThread(String slug_or_id){
        try {
            return distinguish(slug_or_id);
        } catch (EmptyResultDataAccessException e){
            throw new NotFoundException("Can't find thread by id: " + slug_or_id, e);
        }
    }

    public Thread voteForThread(String slug_or_id, Vote vote){
        try {
            Thread thread = distinguish(slug_or_id);
//            try {
//                usersDao.getUser(vote.getNickname());
//            } catch (EmptyResultDataAccessException e){
//                throw new NotFoundException("Can't find user by nickname: " + vote.getNickname(), e);
//            }
            votesDao.addVote(vote, thread.getId());
            thread.setVotes(thread.getVotes() + vote.getVoice());
            threadsDao.voteForThread(thread.getId(), thread.getVotes());
            return thread;
        } catch (EmptyResultDataAccessException e){
            throw new NotFoundException("Can't find thread by id: " + slug_or_id, e);
        }
    }

    public Thread updateThread(String slug_or_id, Thread thread){
        try {
            Thread found = distinguish(slug_or_id);
            thread.checkNull(found);
            threadsDao.updateThread(found.getId(), thread);
            found.setMessage(thread.getMessage());
            found.setTitle(thread.getTitle());
            return found;
        } catch (EmptyResultDataAccessException e){
            throw new NotFoundException("Can't find thread by id: " + slug_or_id, e);
        }
    }

    public synchronized Thread distinguish(String slug_or_id){
        if(slug_or_id != null && slug_or_id.matches("[-+]?\\d*\\.?\\d+")){
            return threadsDao.getThreadById(Integer.parseInt(slug_or_id));
        } else {
            return threadsDao.getThreadBySlug(slug_or_id);
        }
    }
}
