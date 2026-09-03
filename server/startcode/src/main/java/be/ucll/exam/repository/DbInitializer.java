package be.ucll.exam.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import be.ucll.exam.model.User;
import jakarta.annotation.PostConstruct;

@Component
public class DbInitializer {

    private UserRepository userRepository;

    @Autowired
    public DbInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initialize() {

        User user1 = new User("a",  "john.doe@ucll.be", "12345678");
        User user2 = new User("taim",  "jane.toe@ucll.be", "jane1234");
        User user3 = new User("ayman",  "janeayman@ucll.be", "jane1234");
        User user4 = new User("ayman-darodri",  "janeaymandardo@ucll.be", "jane1234");


        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);

    }
}
