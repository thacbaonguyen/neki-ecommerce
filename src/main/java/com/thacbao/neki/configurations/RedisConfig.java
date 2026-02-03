package com.thacbao.neki.configurations;


import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@Slf4j
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.password:#{null}}")
    private String redisPassword;


    private StatefulRedisConnection<String, byte[]> redisConnection;
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(redisHost);
        redisConfig.setPort(redisPort);
        if (redisPassword != null && !redisPassword.isEmpty()) {
            redisConfig.setPassword(redisPassword);
        }
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfig);
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);

        template.setValueSerializer(jsonRedisSerializer);
        template.setHashValueSerializer(jsonRedisSerializer);

        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public ProxyManager<String> proxyManager(RedisConnectionFactory factory) {
        // Cast sang lettuceFactory để lấy config
        LettuceConnectionFactory lettuceFactory = (LettuceConnectionFactory) factory;

        // Lấy standalone config
        var standaloneConfig = lettuceFactory.getStandaloneConfiguration();

        // Tạo RedisURI
        RedisURI.Builder uriBuilder = RedisURI.builder()
                .withHost(standaloneConfig.getHostName())
                .withPort(standaloneConfig.getPort())
                .withDatabase(standaloneConfig.getDatabase());

        if (standaloneConfig.getPassword().isPresent()) {
            uriBuilder.withPassword(standaloneConfig.getPassword().get());
        }

        RedisURI redisUri = uriBuilder.build();

        // Tạo rdclient cho Bucket4j
        RedisClient redisClient = RedisClient.create(redisUri);

        // Tạo connection với codec
        RedisCodec<String, byte[]> codec = RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE);
        this.redisConnection = redisClient.connect(codec);

        log.info("Redis connection for Bucket4j initialized - Host: {}, Port: {}, DB: {}",
                standaloneConfig.getHostName(),
                standaloneConfig.getPort(),
                standaloneConfig.getDatabase());

        // Tạo prxmanager với CAS-based
        return LettuceBasedProxyManager.builderFor(redisConnection)
                .withExpirationStrategy(
                        ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(
                                Duration.ofMinutes(1L)
                        )
                )
                .build();
    }

    @PreDestroy
    public void shutdown() {
        if (redisConnection != null) {
            redisConnection.close();
            log.info("Bucket4j Redis connection closed");
        }
    }
}
