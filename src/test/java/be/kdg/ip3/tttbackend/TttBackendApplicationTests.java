package be.kdg.ip3.tttbackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestContainersConfig.class)
class TttBackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
