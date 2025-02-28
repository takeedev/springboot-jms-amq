package takee.dev.springboot_jms_amq.service;

import org.apache.activemq.Message;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import jakarta.jms.JMSException;
import lombok.extern.slf4j.Slf4j;
import takee.dev.springboot_jms_amq.model.MessageModel;

@Slf4j
@Service
public class AMQConsumer {
    
    @JmsListener(destination = "test-queue")
    public void receiveMessage(String message) {
        log.info("received message : " + message);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            log.error(message, e);
        }
    } 

    @JmsListener(destination = "myQueue")
    public void onMessage(MessageModel myMessage, Message message) throws JMSException {
        String correlationId = message.getJMSCorrelationID();
    }

}
