package be.ucll.exam.service;


import be.ucll.exam.model.Message;
import be.ucll.exam.model.User;
import be.ucll.exam.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class MessageService {


    private final MessageRepository messageRepository;


    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    public Message sendMessage(Message message) {
        message.setTimestamp((java.time.LocalDateTime.now()));
        return messageRepository.save(message);
    }

    public List<Message> getAll() {
        return messageRepository.findAll();
    }


    public List<Message> findChatHistory(String user1, String user2) {
        return messageRepository.findChatHistory(user1,user2);
    }
}
