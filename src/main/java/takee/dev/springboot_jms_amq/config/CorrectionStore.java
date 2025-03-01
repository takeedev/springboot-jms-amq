package takee.dev.springboot_jms_amq.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class CorrectionStore {
    
    private final Map<String, Object> correlationMap = new ConcurrentHashMap<>();

    public void put(String correlationId, Object response) {
        correlationMap.put(correlationId, response);
    }

    public Object get(String correlationId) {
        return correlationMap.get(correlationId);
    }

    public void remove(String correlationId) {
        correlationMap.remove(correlationId);
    }

}
