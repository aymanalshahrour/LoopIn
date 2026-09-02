package be.ucll.exam.service;

import be.ucll.exam.model.User;
import be.ucll.exam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public User addUserAndCheckIfUserNameIsValid(User user) {
        String username = user.getUsername();

        for (User everyUser : getAllUsers()) {
            if (everyUser.getUsername().equals(username)) {
                throw new RuntimeException("User already exists");
            }
        }
        userRepository.save(user);
        return user;
    }

    public boolean findUserAndValidatedUserPassword(String username, String password) {
        boolean found = false;
        for (User everyUser : getAllUsers()) {
            if ((everyUser.getUsername().equals(username) || everyUser.getEmail().equals(username))
                    && everyUser.getPassword().equals(password)) {
                found = true;
                break;
            }
        }
        return found;
    }
}
