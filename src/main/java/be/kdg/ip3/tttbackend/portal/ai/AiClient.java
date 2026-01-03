package be.kdg.ip3.tttbackend.portal.ai;

import be.kdg.ip3.tttbackend.api.dto.AiGameStateDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Component
public class AiClient {
    private final RestTemplate restTemplate;
    @Value("${ai.service.url}")
    private String aiServiceUrl;

    @Value("${ai.service.api-key}")
    private String apiKey;

    public AiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Returns: { "row": 1, "col": 2 }
    public Map<String, Integer> requestAiMove(AiGameStateDto dto) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-Key", apiKey); // 👈 BELANGRIJK

        HttpEntity<AiGameStateDto> request =
                new HttpEntity<>(dto, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                aiServiceUrl,
                HttpMethod.POST,
                request,
                Map.class
        );

        return response.getBody();
    }


}


