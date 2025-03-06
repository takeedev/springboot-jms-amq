package takee.dev.springboot_jms_amq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class CorrelationStoreConfig {

    @Bean
    public Map<String, CompletableFuture<String>> correlationStore() {
        return new ConcurrentHashMap<>();
    }

}
