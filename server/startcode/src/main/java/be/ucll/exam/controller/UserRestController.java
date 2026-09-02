package be.ucll.exam.controller;

import be.ucll.exam.service.UserService;
import be.ucll.exam.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserRestController {
    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/adduser")
    public User addUserAndCheckIfUserNameIsValid(@RequestBody User user){
        return userService.addUserAndCheckIfUserNameIsValid(user);
    }
}
