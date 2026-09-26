package be.ucll.exam.controller;


import be.ucll.exam.model.ChatRequest;
import be.ucll.exam.model.Friendship;
import be.ucll.exam.model.FriendRequestUpdate;
import be.ucll.exam.service.FriendshipService;
import be.ucll.exam.service.RealtimeService;
import be.ucll.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friendship")
public class FriendshipRestController {

    private final FriendshipService friendshipService;
    private final UserService userService;
    private final RealtimeService realtimeService;


    @Autowired
    public FriendshipRestController(FriendshipService friendshipService, UserService userService, RealtimeService realtimeService) {
        this.friendshipService = friendshipService;
        this.userService = userService;
        this.realtimeService = realtimeService;
    }

    @GetMapping
    public List<Friendship> getAll(){
        return friendshipService.getAll();
    }

    @PostMapping("/send")
    public Friendship addFriend(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestBody Friendship friendship){
        friendship.setSenderUsername(userService.getUsernameFromToken(authorizationHeader));
        Friendship savedFriendship = friendshipService.addFriend(friendship);
        realtimeService.broadcast("friend_request_sent", savedFriendship);
        return savedFriendship;
    }

    @PostMapping("/showfriendrequest")
    public List<Friendship> showFriendReq(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestBody ChatRequest request) {
        request.setUser1(userService.getUsernameFromToken(authorizationHeader));
        return friendshipService.showFriendReq(request.getUser1(), request.getUser2());
    }


    @PostMapping("/showonlyuserfriends")
    public List<String> userFriendsList(@RequestHeader(value = "Authorization", required = false) String authorizationHeader){
        String username = userService.getUsernameFromToken(authorizationHeader);
        return friendshipService.userFriendsList(username);
    }

    @PostMapping("/showfriendrequests")
    public List<String> userFriendRequestsList(@RequestHeader(value = "Authorization", required = false) String authorizationHeader){
        String username = userService.getUsernameFromToken(authorizationHeader);
        return friendshipService.userFriendRequestsList(username);
    }

    @PutMapping("/changefriendstatus")
    public List<String> changeFriendStats(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestBody String friendusername){
        String username = userService.getUsernameFromToken(authorizationHeader);
        List<String> requests = friendshipService.changeFriendStats(friendusername, username);
        FriendRequestUpdate update = new FriendRequestUpdate(friendshipService.cleanUsername(friendusername), username);
        realtimeService.broadcast("friend_request_accepted", update);
        return requests;
    }

    @DeleteMapping("/decline")
    public List<String> declineFriend(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestBody String friendusername){
        String username = userService.getUsernameFromToken(authorizationHeader);
        List<String> requests = friendshipService.declineFriendRequest(friendusername, username);
        FriendRequestUpdate update = new FriendRequestUpdate(friendshipService.cleanUsername(friendusername), username);
        realtimeService.broadcast("friend_request_declined", update);
        return requests;
    }

}
