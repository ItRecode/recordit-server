package com.recodeit.server.configuration;

import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

@Configuration
@Profile("test")
public class EmbeddedRedisConfiguration {

    private final RedisServer redisServer;

    protected EmbeddedRedisConfiguration(@Value("${spring.redis.port}") int redisPort) {
        this.redisServer = new RedisServer(redisPort);
        redisServer.start();
    }

    @PreDestroy
    private void stop() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }
}
