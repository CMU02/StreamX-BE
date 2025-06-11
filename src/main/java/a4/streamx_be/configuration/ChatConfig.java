package a4.streamx_be.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {
    @Bean
    public Integer maxMessage() {
        return 20;
    }
}
