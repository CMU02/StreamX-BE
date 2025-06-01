package a4.streamx_be.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {

        LettuceClientConfiguration client = LettuceClientConfiguration.builder()
//                배포시 사용
//                .useSsl().and()
                .commandTimeout(Duration.ofSeconds(2))
                .shutdownTimeout(Duration.ZERO)
                .build();

        RedisStandaloneConfiguration config
                = new RedisStandaloneConfiguration("localhost", 6379);

        return new LettuceConnectionFactory(config, client);
    }

    @Bean
    public ReactiveRedisTemplate<String, String> defaultRedisTemplate(
            ReactiveRedisConnectionFactory factory
    ) {
        return new ReactiveRedisTemplate<>(factory, RedisSerializationContext.string());
    }
}
