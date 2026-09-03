package be.ucll.exam.model;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Messages")

public class Message {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String senderUsername;

    private String receiverUsername;

    private String content;

    private LocalDateTime timestamp;


    public Message(){

    }
    public Message(String senderUsername, String receiverUsername, String content){
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.content = content;
        this.timestamp = LocalDateTime.now();

    }

    public Long getId() {return id;}
    public void setId(Long id){
        this.id = id;
    }
    public String getSenderUsername(){return senderUsername;}
    public void setSenderUsername(String senderUsername){
        this.senderUsername = senderUsername;
    }
    public String getReceiverUsername(){
        return receiverUsername;
    }
    public void setReceiverUsername(String receiverUsername){
        this.receiverUsername = receiverUsername;

    }
    public String getContent(){
        return content;
    }
    public void setContent(String content){
        this.content = content;
    }
    public LocalDateTime getTimestamp(){
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp){
        this.timestamp = timestamp;
    }
}
