package takee.dev.springboot_jms_amq.service;

import java.util.UUID;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import takee.dev.springboot_jms_amq.model.MessageModel;

@Service
@RequiredArgsConstructor
public class AMQProducer {
    
    private final JmsTemplate jmsTemplate;

    public void sendMessage(String destination, String message) {
        jmsTemplate.convertAndSend(destination, message);
    }

    public void sendMessageDAO(String destination, MessageModel messageObj) {
         String correlationId = UUID.randomUUID().toString();
        jmsTemplate.send(destination, session -> {
            TextMessage message = session.createTextMessage();
            message.setText(messageObj.toString()); 
            message.setJMSCorrelationID(correlationId);
            return message;
        });
    }


}
