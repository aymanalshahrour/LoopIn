package be.ucll.exam.service;

import be.ucll.exam.model.Message;
import be.ucll.exam.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "*")
public class MessageRestController {
    @Autowired
    private MessageRepository messageRepository;

    @PostMapping("/send")
    public Message sendMessage(@RequestBody Message message){
        message.setTimestamp((java.time.LocalDateTime.now()));
        return messageRepository.save(message);
    }
}


