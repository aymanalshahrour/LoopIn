package be.ucll.exam.controller;


import be.ucll.exam.model.ChatRequest;
import be.ucll.exam.model.Friendship;
import be.ucll.exam.model.Message;
import be.ucll.exam.model.User;
import be.ucll.exam.service.FriendshipService;
import be.ucll.exam.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/friendship")
@CrossOrigin(origins = "http://localhost:63343")
public class FriendshipRestController {

    private final FriendshipService friendshipService;


    @Autowired
    public FriendshipRestController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @GetMapping
    public List<Friendship> getAll(){
        return friendshipService.getAll();
    }

    @PostMapping("/send")
    public Friendship addFriend(@RequestBody Friendship friendship){
        return friendshipService.addFriend(friendship);
    }

    @PostMapping("/showfriendrequest")
    public List<Friendship> showFriendReq(@RequestBody ChatRequest request) {
        return friendshipService.showFriendReq(request.getUser1(), request.getUser2());
    }


    @PostMapping("/showonlyuserfriends")
    public List<String> userFriendsList(@RequestBody Map<String, String> payload){
        String username = payload.get("user");
        return friendshipService.userFriendsList(username);
    }
}
