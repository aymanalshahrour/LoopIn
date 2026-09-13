package be.ucll.exam.service;


import be.ucll.exam.model.Friendship;
import be.ucll.exam.repository.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;


    @Autowired
    public FriendshipService(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    public List<Friendship> getAll() {
        return friendshipRepository.findAll();
    }

    public Friendship addFriend(Friendship friendship) {
        friendship.setStatus("Pending");
        return friendshipRepository.save(friendship);
    }

    public List<Friendship> showFriendReq(String user1, String user2) {
        return friendshipRepository.findFriendshipBetweenUsers(user1,user2);
    }
}
