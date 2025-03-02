package takee.dev.springboot_jms_amq.service;

import java.util.UUID;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import takee.dev.springboot_jms_amq.config.CorrelationStore;
import takee.dev.springboot_jms_amq.model.MessageModel;

@Slf4j
@Service
@RequiredArgsConstructor
public class AMQProducer {

    private final JmsTemplate jmsTemplate;

    private final CorrelationStore correctionStore;

    private final ObjectMapper objectMapper;

    public void sendMessage(String destination, String message) {
        jmsTemplate.convertAndSend(destination, message);
    }

    public void sendMessageDAO(String destination, MessageModel messageObj) {
        String correlationId = UUID.randomUUID().toString();
        correctionStore.put(correlationId, messageObj);
        log.info("Request => " + correctionStore.get(correlationId));
        jmsTemplate.send(destination, session -> {
            TextMessage message = session.createTextMessage();
            String jsonMessage = convertObjectToJson(messageObj);
            message.setText(jsonMessage);
            message.setJMSCorrelationID(correlationId);
            return message;
        });
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


}
