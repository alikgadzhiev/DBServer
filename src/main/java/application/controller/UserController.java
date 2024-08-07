package application.controller;

import application.models.User;
import application.exceptions.DuplicateException;
import application.exceptions.NotFoundException;
import application.response.DuplicateResponse;
import application.response.NotFoundResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import application.service.UserService;

import org.slf4j.Logger;

@RestController
@RequestMapping("/api/user/{nickname}")
public class UserController {

    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<?> createUser(@PathVariable("nickname") String nickname, @RequestBody User user){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(nickname, user));
        } catch(DuplicateException e){
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(userService.getUsers(nickname, user.getEmail()));
        } catch(NotFoundException e){
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new NotFoundResponse(e.getMessage()));
        }
    }

    @GetMapping("/profile")
    @ResponseBody
    public ResponseEntity<?> getUser(@PathVariable("nickname") String nickname){
        try {
            return ResponseEntity.ok(userService.getUser(nickname));
        } catch(NotFoundException e){
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse(e.getMessage()));
        }
    }

    @PostMapping("/profile")
    @ResponseBody
    public ResponseEntity<?> updateUser(@PathVariable("nickname") String nickname, @RequestBody User user){
        try {
            return ResponseEntity.ok(userService.updateUser(nickname, user));
        } catch(NotFoundException e){
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse(e.getMessage()));
        } catch (DuplicateException e){
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new DuplicateResponse(e.getMessage()));
        }
    }
}



