package takee.dev.springboot_jms_amq.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import takee.dev.springboot_jms_amq.model.MessageModel;

@Slf4j
@Service
@RequiredArgsConstructor
public class AMQProducer {

    private final JmsTemplate jmsTemplate;

    private final ObjectMapper objectMapper;

    private final Map<String, CompletableFuture<MessageModel>> correlationStore;

    public void sendMessage(String destination, String message) {
        jmsTemplate.convertAndSend(destination, message);
    }

    private String convertObjectToJson(MessageModel messageObj) throws JMSException {
        String jsonMessage;
        try {
            jsonMessage = objectMapper.writeValueAsString(messageObj);
        } catch (JsonProcessingException e) {
            throw new JMSException("Error converting object to JSON");
        }
        return jsonMessage;
    }

    public CompletableFuture<MessageModel> sendMsg(MessageModel messageModel) {
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<MessageModel> futureResponse = new CompletableFuture<>();
        correlationStore.put(correlationId, futureResponse);
        try {
            jmsTemplate.send("test-queue", session -> {
                TextMessage message = session.createTextMessage(convertObjectToJson(messageModel));
                message.setJMSCorrelationID(correlationId);
                return message;
            });
            log.info("✅ Sent message: {} | CorrelationID: {}", messageModel, correlationId);
        } catch (JmsException e) {
            correlationStore.remove(correlationId);
            futureResponse.completeExceptionally(e);
        }
        return futureResponse;
    }

}
