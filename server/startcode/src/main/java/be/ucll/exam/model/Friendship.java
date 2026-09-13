package be.ucll.exam.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "friendships")
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String senderUsername;

    private String receiverUsername;

    private String status;

    public Friendship(String senderUsername, String receiverUsername, String status) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.status = status;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    protected Friendship() {}

}