package application.controller;

import application.models.Post;
import application.models.Thread;
import application.models.ThreadUpdate;
import application.models.Vote;
import application.exceptions.DuplicateException;
import application.exceptions.NotFoundException;
import application.response.DuplicateResponse;
import application.response.NotFoundResponse;
import org.aspectj.weaver.ast.Not;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import application.service.ThreadService;

import java.util.ArrayList;

import static org.apache.commons.lang3.StringUtils.join;

@RestController
public class ThreadController {
    private static Logger LOGGER = LoggerFactory.getLogger(ThreadController.class);
    private final ThreadService threadService;

    @Autowired
    public ThreadController(ThreadService threadService) {
        this.threadService = threadService;
    }

    // fully completed
    @PostMapping("/api/forum/{slug}/create")
    @ResponseBody
    public ResponseEntity<?> createThread(@PathVariable("slug") String slug, @RequestBody Thread thread) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(threadService.createThread(slug, thread));
        } catch (DuplicateException e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(threadService.getThread(slug));
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse(e.getMessage()));
        }
    }

    @PostMapping("/api/thread/{slug_or_id}/create")
    @ResponseBody
    public ResponseEntity<?> createPosts(@PathVariable("slug_or_id") String slug_or_id, @RequestBody ArrayList<Post> posts) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(threadService.createPosts(slug_or_id, posts));
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse(e.getMessage()));
        } catch (DuplicateException e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new DuplicateResponse(e.getMessage()));
        }
    }

    @PostMapping("/api/thread/{slug_or_id}/details")
    @ResponseBody
    public ResponseEntity<?> updateThread(@PathVariable("slug_or_id") String slug_or_id, @RequestBody Thread thread) {
        try {
            return ResponseEntity.ok().body(threadService.updateThread(slug_or_id, thread));
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse(e.getMessage()));
        }
    }

    @GetMapping("/api/thread/{slug_or_id}/posts")
    @ResponseBody
    public ResponseEntity<?> getPosts(@PathVariable("slug_or_id") String slug_or_id, @RequestParam(value = "limit", required = false, defaultValue = "100") int limit,
                                      @RequestParam(value = "since", required = false, defaultValue = "0") String since, @RequestParam(value = "sort", required = false, defaultValue = "flat") String sort,
                                      @RequestParam(value = "desc", required = false, defaultValue = "false") boolean desc) {
        try {
            return ResponseEntity.ok().body(threadService.getPosts(slug_or_id, limit, Long.parseLong(since), sort, desc));
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse(e.getMessage()));
        }
    }

    @GetMapping("/api/thread/{slug_or_id}/details")
    @ResponseBody
    public ResponseEntity<?> getThread(@PathVariable("slug_or_id") String slug_or_id) {
        try {
            return ResponseEntity.ok().body(threadService.getThread(slug_or_id));
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse(e.getMessage()));
        }
    }

    @PostMapping("/api/thread/{slug_or_id}/vote")
    @ResponseBody
    public ResponseEntity<?> voteForThread(@PathVariable("slug_or_id") String slug_or_id, @RequestBody Vote vote) {
        try {
            return ResponseEntity.ok().body(threadService.voteForThread(slug_or_id, vote));
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse(e.getMessage()));
        }
    }
}
