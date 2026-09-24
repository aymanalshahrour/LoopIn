package be.ucll.exam.service;

import be.ucll.exam.model.RealtimeMessage;
import be.ucll.exam.websocket.RealtimeWebSocketHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class RealtimeService {

    private final RealtimeWebSocketHandler realtimeWebSocketHandler;
    private final ObjectMapper objectMapper;

    public RealtimeService(RealtimeWebSocketHandler realtimeWebSocketHandler, ObjectMapper objectMapper) {
        this.realtimeWebSocketHandler = realtimeWebSocketHandler;
        this.objectMapper = objectMapper;
    }

    public void broadcast(String type, Object data) {
        try {
            realtimeWebSocketHandler.broadcast(objectMapper.writeValueAsString(new RealtimeMessage(type, data)));
        } catch (JsonProcessingException error) {
            throw new IllegalStateException("Could not send realtime update", error);
        }
    }
}
