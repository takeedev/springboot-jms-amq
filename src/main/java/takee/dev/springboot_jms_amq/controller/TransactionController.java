package takee.dev.springboot_jms_amq.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.media.Schema;
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
    public CompletableFuture<MessageModel> sendMessage(@RequestBody MessageModel message) {
        return amqProducer.sendMsg(message).orTimeout(1, TimeUnit.MINUTES);
    }

}
