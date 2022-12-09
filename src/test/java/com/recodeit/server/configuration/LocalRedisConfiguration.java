package com.recodeit.server.configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

@Configuration
@Profile("test")
public class LocalRedisConfiguration {

    private final int redisPort;

    private RedisServer redisServer;

    public LocalRedisConfiguration(@Value("${spring.redis.port}") int redisPort) {
        this.redisPort = redisPort;
    }

    @PostConstruct
    public void startRedis() {
        redisServer = new RedisServer(redisPort);
        redisServer.start();
    }

    @PreDestroy
    public void stop() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }
}
