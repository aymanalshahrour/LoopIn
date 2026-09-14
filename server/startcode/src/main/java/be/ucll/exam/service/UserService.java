package be.ucll.exam.service;

import be.ucll.exam.model.User;
import be.ucll.exam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {
    private final UserRepository userRepository;
    private static final Duration SESSION_TIMEOUT = Duration.ofHours(24);
    private final Map<String, UserSession> sessions = new ConcurrentHashMap<>();

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

    public LoginResponse findUserAndValidatedUserPassword(String username, String password) {
        for (User everyUser : getAllUsers()) {
            if ((everyUser.getUsername().equals(username) || everyUser.getEmail().equals(username))
                    && everyUser.getPassword().equals(password)) {
                String token = UUID.randomUUID().toString();
                Instant expiresAt = Instant.now().plus(SESSION_TIMEOUT);
                sessions.put(token, new UserSession(everyUser.getUsername(), expiresAt));
                return new LoginResponse(token, everyUser.getUsername(), expiresAt.toString());
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username/email or password");
    }

    public String getUsernameFromToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing session token");
        }

        String token = authorizationHeader.substring("Bearer ".length());
        UserSession session = sessions.get(token);

        if (session == null || Instant.now().isAfter(session.expiresAt())) {
            sessions.remove(token);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Session expired or invalid");
        }

        return session.username();
    }

    public record LoginResponse(String token, String username, String expiresAt) {}

    private record UserSession(String username, Instant expiresAt) {}
}
