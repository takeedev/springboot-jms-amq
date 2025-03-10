package takee.dev.springboot_jms_amq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import takee.dev.springboot_jms_amq.model.MessageModel;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class CorrelationStoreConfig {

    @Bean
    public Map<String, CompletableFuture<MessageModel>> correlationStore() {
        return new ConcurrentHashMap<>();
    }

}
