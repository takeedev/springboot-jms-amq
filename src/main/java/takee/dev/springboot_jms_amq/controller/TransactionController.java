package takee.dev.springboot_jms_amq.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import takee.dev.springboot_jms_amq.model.MessageModel;
import takee.dev.springboot_jms_amq.service.AMQProducer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


@RestController
@AllArgsConstructor
@RequestMapping(value = "/trans")
public class TransactionController {

    private final AMQProducer amqProducer;

    @GetMapping("/sendOneMessage")
    public String sendOneMessage(@RequestParam String param) {
        amqProducer.sendMessage("test-queue-thread", param);
        return "send message: " + param;
    }

    @GetMapping("/sendConcurrenceTestMessage")
    public String sendConcurrenceTestMessage(
            @RequestParam @Schema(example = "Test Message") String param,
            @RequestParam @Schema(example = "1000") int count) {
        for (int i = 0; i < count; i++) {
            amqProducer.sendMessage("test-queue-multi-thread-", param + " " + i);
        }
        return "send message : " + param;
    }

    @PostMapping("/sendMessage")
    public CompletableFuture<String> sendMessage(@RequestBody MessageModel message) {
        MessageModel request = new MessageModel("1", "Test Naja");
        return amqProducer.sendMsg(request).orTimeout(1, TimeUnit.MINUTES);
    }

}
