package a4.streamx_be;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableJpaAuditing
@EnableFeignClients
@EnableScheduling
@SpringBootApplication
public class StreamxBeApplication {
    public static void main(String[] args) {
        SpringApplication.run(StreamxBeApplication.class, args);
    }
}