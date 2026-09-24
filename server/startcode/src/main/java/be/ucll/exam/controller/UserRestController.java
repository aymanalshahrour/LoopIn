package be.ucll.exam.controller;

import be.ucll.exam.model.LoginRequest;
import be.ucll.exam.service.UserService;
import be.ucll.exam.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:63343")
public class UserRestController {
    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public UserService.LoginResponse findUserAndValidatedUserPassword(@RequestBody LoginRequest loginRequest){
        return userService.findUserAndValidatedUserPassword(
                loginRequest.getUsernameOrEmail(),
                loginRequest.getPassword()
        );
    }

    @PostMapping("/adduser")
    public User addUserAndCheckIfUserNameIsValid(@RequestBody User user){
        return userService.addUserAndCheckIfUserNameIsValid(user);
    }
}
