package com.andrei.ecommerce_innowise.config;

import java.time.Duration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;


@Configuration
@EnableCaching
public class RedisConfig {
  private static final int CACHE_LIFE_DURATION_MINUTES = 10;

  @Bean
  public RedisCacheConfiguration createRedisConfig(){

    return RedisCacheConfiguration
        .defaultCacheConfig()
        .disableCachingNullValues()
        .entryTtl(Duration.ofMinutes(CACHE_LIFE_DURATION_MINUTES))
        .serializeValuesWith(
            RedisSerializationContext
                .SerializationPair
                .fromSerializer(GenericJacksonJsonRedisSerializer
                    .builder().build())
        );
  }

}
