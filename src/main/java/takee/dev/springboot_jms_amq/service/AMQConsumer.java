package takee.dev.springboot_jms_amq.service;

import org.apache.activemq.Message;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import takee.dev.springboot_jms_amq.model.MessageModel;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class AMQConsumer {

    private final JmsTemplate jmsTemplate;

    private final Map<String, CompletableFuture<String>> correlationStore;

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
            String processedData = "Processed: " + requestPayload;
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
            log.info("Received response: {} | CorrelationID: {}", responseText, correlationId);
            CompletableFuture<String> future = correlationStore.remove(correlationId);
            if (future != null) {
                future.complete(responseText);
            }
        } catch (Exception e) {
            log.error("Error receiving response: {}", e.getMessage(), e);
        }
    }
}
