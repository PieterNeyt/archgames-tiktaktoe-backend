package be.kdg.ip3.tttbackend;

import be.kdg.ip3.tttbackend.portal.messaging.sender.tttMessagePublisher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestContainersConfig.class)
class TttBackendApplicationTests {

    @MockitoBean
    private tttMessagePublisher tttMessagePublisher;
    @Test
    void contextLoads() {
    }
}
