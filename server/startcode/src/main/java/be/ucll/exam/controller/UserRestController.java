package be.ucll.exam.controller;

import be.ucll.exam.service.UserService;
import be.ucll.exam.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:63343")
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

//  users/user/{USERname}/{pass}
    @GetMapping("/user/{username}/{password}")
    public boolean findUserAndValidatedUserPassword(@PathVariable String username,@PathVariable String password){
        return userService.findUserAndValidatedUserPassword(username, password);
    }

    @PostMapping("/adduser")
    public User addUserAndCheckIfUserNameIsValid(@RequestBody User user){
        return userService.addUserAndCheckIfUserNameIsValid(user);
    }
}
