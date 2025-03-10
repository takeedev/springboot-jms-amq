package takee.dev.springboot_jms_amq.service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.apache.activemq.Message;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import takee.dev.springboot_jms_amq.model.MessageModel;

@Slf4j
@Service
@RequiredArgsConstructor
public class AMQConsumer {

    private final JmsTemplate jmsTemplate;

    private final ObjectMapper mapper;

    private final Map<String, CompletableFuture<MessageModel>> correlationStore;

    @JmsListener(destination = "test-queue-multi-thread-")
    public void messageThread(String message) {
        log.info("received message : {}", message);
    }


    @JmsListener(destination = "test-queue-multi-thread-")
    public void receiveMessage(String message) throws InterruptedException {
        log.info("received message multi thread : {}", message);
        Thread.sleep(3000);
    }

    @JmsListener(destination = "test-queue")
    public void consume(Message message) {
        try {
            String correlationId = message.getJMSCorrelationID();
            String requestPayload = ((TextMessage) message).getText();
            log.info("Received message: {} | CorrelationID: {}", requestPayload, correlationId);
            Thread.sleep(3000);
            String processedData = requestPayload;
            jmsTemplate.convertAndSend("response-queue", processedData, msg -> {
                msg.setJMSCorrelationID(correlationId);
                return msg;
            });
            log.info("Sent Response: {} | CorrelationID: {}", processedData, correlationId);
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
        }
    }

    @JmsListener(destination = "response-queue")
    public void receiveResponse(Message message) {
        try {
            String correlationId = message.getJMSCorrelationID();
            String responseText = ((TextMessage) message).getText();
            MessageModel messageResponse = mapper.readValue(((TextMessage) message).getText(), MessageModel.class);
            log.info("Received response: {} | CorrelationID: {}", responseText, correlationId);
            CompletableFuture<MessageModel> future = correlationStore.remove(correlationId);
            if (future != null) {
                future.complete(messageResponse);
            }
        } catch (Exception e) {
            log.error("Error receiving response: {}", e.getMessage(), e);
        }
    }
}
