package takee.dev.springboot_jms_amq.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.AllArgsConstructor;
import takee.dev.springboot_jms_amq.model.MessageModel;
import takee.dev.springboot_jms_amq.service.AMQProducer;


@RestController
@AllArgsConstructor
@RequestMapping(value = "/trans")
public class TransactionController {

    private final AMQProducer amqProducer;

    @GetMapping("/sendOneMessage")
    public String sendOneMessage(@RequestParam String param) {
        amqProducer.sendMessage("test-queue", param);
        return "send message: " + param;
    }

    @GetMapping("/sendConcurenceTestMessage")
    public String sendConcurenceTestMessage(@RequestParam @Schema(example = "Test Message") String param , @RequestParam @Schema(example = "1000") int count) {
        for (int i = 0; i < count; i++) {
            amqProducer.sendMessage("test-queue", param + " " + i);
        } 
        return "send message : " + param;
    }

    @PostMapping("/sendCorrectionIDTestMessage")
    public String sendCorrectionIDTestMessage(@RequestBody MessageModel param) {
        MessageModel request = new MessageModel("1", "Test Naja");
        amqProducer.sendMessageDAO("test-queue", request);
        return "send message : " + param;
    }
    
}