package be.kdg.ip3.tttbackend.infrastructure.ai;

import be.kdg.ip3.tttbackend.api.dto.AiGameStateDto;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class AiClient {
    private final RestTemplate restTemplate;
    private final String aiServiceUrl = "http://localhost:9001/ai/move";

    public AiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Returns: { "row": 1, "col": 2 }
    public Map<String, Integer> requestAiMove(AiGameStateDto dto) {
        return restTemplate.postForObject(aiServiceUrl, dto, Map.class);
    }


}


