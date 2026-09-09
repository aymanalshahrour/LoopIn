package be.ucll.exam.controller;

import be.ucll.exam.model.ChatRequest;
import be.ucll.exam.model.Message;
import be.ucll.exam.model.User;
import be.ucll.exam.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "http://localhost:63343")

public class MessageRestController {
    private final MessageService messageService;

    @Autowired
    public MessageRestController(MessageService messageService) {
        this.messageService = messageService;
    }
    @GetMapping
    public List<Message> getAll(){
        return messageService.getAll();
    }

    @PostMapping("/send")
    public Message sendMessage(@RequestBody Message message){
        return messageService.sendMessage(message);
    }

    @PostMapping("/messageuserandreciver")
    public List<Message> showMsgInChat(@RequestBody ChatRequest request) {
        return messageService.findChatHistory(request.getUser1(), request.getUser2());
    }





}

