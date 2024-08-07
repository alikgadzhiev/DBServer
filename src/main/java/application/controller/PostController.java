package application.controller;

import application.exceptions.NotFoundException;
import application.models.Post;
import application.models.PostUpdate;
import application.response.NotFoundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import application.service.PostService;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/post/{id}")
public class PostController {

    private static Logger LOGGER = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/details")
    @ResponseBody
    public ResponseEntity<?> updatePost(@PathVariable("id") long id, @RequestBody Post post){
        try {
            return ResponseEntity.ok().body(postService.updatePost(id, post));
        } catch(NotFoundException e){
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse(e.getMessage()));
        }
    }

    @GetMapping("/details")
    @ResponseBody
    public ResponseEntity<?> getInfo(@PathVariable("id") long id, @RequestParam(value = "related", required = false) ArrayList<String> related){
        try {
            return ResponseEntity.ok().body(postService.getInfo(id, related));
        } catch (NotFoundException e){
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse(e.getMessage()));
        }
    }
}
