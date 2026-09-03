package be.ucll.exam.repository;
import be.ucll.exam.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{
    //no way dat ik ooit dat zelf had kunnen doen maar fetches text between person a and person b ordered by time.
    @Query("SELECT m FROM Message m Where (m.senderUsername = :user1 AND m.receiverUsername = :user2) OR (m.senderUsername = :user2 AND m.receiverUsername = :user1) ORDER BY m.timestamp ASC")
    List<Message> findChatHistory(@Param("user1") String user1, @Param("user2") String user2);

}
