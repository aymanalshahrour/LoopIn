package be.ucll.exam.repository;


import be.ucll.exam.model.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {


    @Query("SELECT f FROM Friendship f WHERE (LOWER(f.senderUsername) = LOWER(:user1) AND LOWER(f.receiverUsername) = LOWER(:user2)) OR (LOWER(f.senderUsername) = LOWER(:user2) AND LOWER(f.receiverUsername) = LOWER(:user1))")
    List<Friendship> findFriendshipBetweenUsers(@Param("user1") String user1, @Param("user2") String user2);
}
