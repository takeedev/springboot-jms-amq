package takee.dev.springboot_jms_amq.service;

import org.apache.activemq.Message;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
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
public class AMQConsumer {

    private final CorrelationStore correlationStore;

    private final ObjectMapper objectMapper;

    @JmsListener(destination = "test-queue")
    public void receiveMessage(String message) throws InterruptedException {
        log.info("received message : " + message);
        Thread.sleep(3000);
    }

    @JmsListener(destination = "test-queue")
    public void onMessage(Message message) throws JMSException {
        if (message instanceof TextMessage) {
            String correlationId = message.getJMSCorrelationID();
            String receivedJson = ((TextMessage) message).getText();
            log.info(correlationId + " => " + receivedJson);

            try {
                MessageModel responseObj = objectMapper.readValue(receivedJson, MessageModel.class);
                MessageModel requestObj = (MessageModel) correlationStore.get(correlationId);
                log.info(requestObj.toString());
                if (requestObj != null) {
                    log.info("Response received for CorrelationID " + correlationId + ": "
                            + responseObj);
                    correlationStore.remove(correlationId);
                }
            } catch (Exception e) {
                log.error("Error parsing JSON: " + e.getMessage());
            }
        }
    }

}
