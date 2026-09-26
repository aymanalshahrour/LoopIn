package be.ucll.exam.controller;

import be.ucll.exam.model.ChatRequest;
import be.ucll.exam.model.Message;
import be.ucll.exam.service.MessageService;
import be.ucll.exam.service.RealtimeService;
import be.ucll.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "*")

public class MessageRestController {
    private final MessageService messageService;
    private final UserService userService;
    private final RealtimeService realtimeService;

    @Autowired
    public MessageRestController(MessageService messageService, UserService userService, RealtimeService realtimeService) {
        this.messageService = messageService;
        this.userService = userService;
        this.realtimeService = realtimeService;
    }

    @GetMapping
    public List<Message> getAll(){
        return messageService.getAll();
    }

    @PostMapping("/send")
    public Message sendMessage(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestBody Message message){
        message.setSenderUsername(userService.getUsernameFromToken(authorizationHeader));
        Message savedMessage = messageService.sendMessage(message);
        realtimeService.broadcast("message_sent", savedMessage);
        return savedMessage;
    }

    @PostMapping("/messageuserandreciver")
    public List<Message> showMsgInChat(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestBody ChatRequest request) {
        request.setUser1(userService.getUsernameFromToken(authorizationHeader));
        return messageService.findChatHistory(request.getUser1(), request.getUser2());
    }





}

