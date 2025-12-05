package be.kdg.ip3.tttbackend.portal.ai;

import be.kdg.ip3.tttbackend.api.dto.AiGameStateDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Component
public class AiClient {
    private final RestTemplate restTemplate;
    @Value("${ai.service.url}")
    private String aiServiceUrl;

    public AiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Returns: { "row": 1, "col": 2 }
    public Map<String, Integer> requestAiMove(AiGameStateDto dto) {
        return restTemplate.postForObject(aiServiceUrl, dto, Map.class);
    }


}


