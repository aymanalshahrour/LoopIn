package be.ucll.exam.service;


import be.ucll.exam.model.Friendship;
import be.ucll.exam.repository.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
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

    public List<String> userFriendsList(String user) {
        List<String> namesOfFriends = new ArrayList<>();

        for (Friendship friendship : friendshipRepository.findFriendshipsForUserByStatus(user, "friends")) {

            if (friendship.getReceiverUsername() != null && friendship.getSenderUsername() != null) {

                if (friendship.getSenderUsername().equalsIgnoreCase(user)) {
                    namesOfFriends.add(friendship.getReceiverUsername());
                }
                else if (friendship.getReceiverUsername().equalsIgnoreCase(user)) {
                    namesOfFriends.add(friendship.getSenderUsername());
                }
            }
        }

        return namesOfFriends;
    }

    public List<String> userFriendRequestsList(String user) {
        List<String> namesOfUsersRequestingFriendship = new ArrayList<>();

        for (Friendship friendship : friendshipRepository.findReceivedFriendshipsByStatus(user, "Pending")) {
            if (friendship.getSenderUsername() != null) {
                namesOfUsersRequestingFriendship.add(friendship.getSenderUsername());
            }
        }

        return namesOfUsersRequestingFriendship;
    }

    public List<String> changeFriendStats(String friendusername, String currentuser) {
        friendusername = cleanUsername(friendusername);
        List<Friendship> relationShip = friendshipRepository.findFriendshipBetweenUsers(currentuser, friendusername);
        Friendship relation = findPendingRequestForCurrentUser(relationShip, currentuser);

        relation.setStatus("friends");
        friendshipRepository.save(relation);

        return userFriendRequestsList(currentuser);
    }

    public List<String> declineFriendRequest(String friendusername, String currentuser) {
        friendusername = cleanUsername(friendusername);
        List<Friendship> relationShip = friendshipRepository.findFriendshipBetweenUsers(currentuser, friendusername);
        Friendship relation = findPendingRequestForCurrentUser(relationShip, currentuser);

        friendshipRepository.delete(relation);

        return userFriendRequestsList(currentuser);
    }

    private Friendship findPendingRequestForCurrentUser(List<Friendship> relationShip, String currentuser) {
        for (Friendship relation : relationShip) {
            if (relation.getReceiverUsername() != null
                    && relation.getReceiverUsername().equalsIgnoreCase(currentuser)
                    && relation.getStatus() != null
                    && relation.getStatus().equalsIgnoreCase("Pending")) {
                return relation;
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Friend request not found");
    }

    public String cleanUsername(String username) {
        if (username == null) {
            return "";
        }

        username = username.trim();

        if (username.startsWith("\"") && username.endsWith("\"") && username.length() >= 2) {
            username = username.substring(1, username.length() - 1);
        }

        return username.trim();
    }
}
