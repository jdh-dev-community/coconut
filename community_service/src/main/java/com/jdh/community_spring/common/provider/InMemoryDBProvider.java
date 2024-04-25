package com.jdh.community_spring.common.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryDBProvider {

  private final StringRedisTemplate stringRedisTemplate;

  public void setTemperarily(String key, String value, long second) {
    log.info("save in redis with key: {}, value: {}", key, value);
    stringRedisTemplate.opsForValue().set(key, value, Duration.ofSeconds(second));
  }
  public void set(String key, String value) {
    stringRedisTemplate.opsForValue().set(key, value);
  }

  public String get(String key) {
    return stringRedisTemplate.opsForValue().get(key);
  }

}
