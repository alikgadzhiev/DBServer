package application.controller;

import application.models.Forum;
import application.exceptions.DuplicateException;
import application.exceptions.NotFoundException;
import application.response.NotFoundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import application.service.ForumService;

@RestController
@RequestMapping("/api/forum")
public class ForumController {
    private static Logger LOGGER = LoggerFactory.getLogger(ForumController.class);

    private final ForumService forumService;

    @Autowired
    public ForumController(ForumService forumService) {
        this.forumService = forumService;
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<?> createForum(@RequestBody Forum forum){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(forumService.createForum(forum));
        } catch (DuplicateException e){
            synchronized (this) {
                LOGGER.error(e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(forumService.getForum(forum.getSlug()));
            }
        } catch (NotFoundException e){
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse(e.getMessage()));
        }
    }

    @GetMapping("/{slug}/users")
    @ResponseBody
    public ResponseEntity<?> getUsers(@PathVariable("slug") String slug, @RequestParam(value = "limit", required = false, defaultValue = "100") int limit,
                                      @RequestParam(value = "since", required = false, defaultValue = "") String since, @RequestParam(value = "desc", required = false, defaultValue = "false") boolean desc){
        try {
            return ResponseEntity.ok().body(forumService.getUsers(slug, limit, since, desc));
        } catch (NotFoundException e){
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse(e.getMessage()));
        }
    }

    @GetMapping("/{slug}/threads")
    @ResponseBody
    public ResponseEntity<?> getThreads(@PathVariable("slug") String slug, @RequestParam(value = "limit", required = false, defaultValue = "100") int limit,
                                        @RequestParam(value = "since", required = false) String since, @RequestParam(value = "desc", required = false, defaultValue = "false") boolean desc){
        try {
            return ResponseEntity.ok().body(forumService.getThreads(slug, limit, since, desc));
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse(e.getMessage()));
        }
    }

    @GetMapping("/{slug}/details")
    @ResponseBody
    public ResponseEntity<?> getForum(@PathVariable("slug") String slug){
        try {
            return ResponseEntity.ok().body(forumService.getForum(slug));
        } catch(NotFoundException e){
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse(e.getMessage()));
        }
    }

}
