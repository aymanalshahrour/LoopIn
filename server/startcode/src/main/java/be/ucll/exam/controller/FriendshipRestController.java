package be.ucll.exam.controller;


import be.ucll.exam.model.ChatRequest;
import be.ucll.exam.model.Friendship;
import be.ucll.exam.model.Message;
import be.ucll.exam.model.User;
import be.ucll.exam.service.FriendshipService;
import be.ucll.exam.service.MessageService;
import be.ucll.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/friendship")
@CrossOrigin(origins = "http://localhost:63343")
public class FriendshipRestController {

    private final FriendshipService friendshipService;
    private final UserService userService;


    @Autowired
    public FriendshipRestController(FriendshipService friendshipService, UserService userService) {
        this.friendshipService = friendshipService;
        this.userService = userService;
    }

    @GetMapping
    public List<Friendship> getAll(){
        return friendshipService.getAll();
    }

    @PostMapping("/send")
    public Friendship addFriend(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestBody Friendship friendship){
        friendship.setSenderUsername(userService.getUsernameFromToken(authorizationHeader));
        return friendshipService.addFriend(friendship);
    }

    @PostMapping("/showfriendrequest")
    public List<Friendship> showFriendReq(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestBody ChatRequest request) {
        request.setUser1(userService.getUsernameFromToken(authorizationHeader));
        return friendshipService.showFriendReq(request.getUser1(), request.getUser2());
    }


    @PostMapping("/showonlyuserfriends")
    public List<String> userFriendsList(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestBody Map<String, String> payload){
        String username = userService.getUsernameFromToken(authorizationHeader);
        return friendshipService.userFriendsList(username);
    }
}
