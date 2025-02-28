package takee.dev.springboot_jms_amq.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import takee.dev.springboot_jms_amq.service.AMQProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(value = "/trans")
@AllArgsConstructor
public class TransactionController {

    private final AMQProducer amqProducer;

    @GetMapping("/sendOneMessage")
    public String sendOneMessage(@RequestParam String param) {
        amqProducer.sendMessage("test-queue", param);
        return "send message : " + param;
    }

    @GetMapping("/sendConMessage")
    public String sendConMessage(@RequestParam @Schema(example = "Test Message") String param , @RequestParam @Schema(example = "1000") int count) {
        for (int i = 0; i < count; i++) {
            amqProducer.sendMessage("test-queue", param + " " + i);
        } 
        return "send message : " + param;
    }
    
}