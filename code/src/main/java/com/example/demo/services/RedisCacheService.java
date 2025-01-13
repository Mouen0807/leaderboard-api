package com.example.demo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class RedisCacheService {
    private static final Logger logger = LoggerFactory.getLogger(RedisCacheService.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void revokeToken(String tokenId, String tokenType, long timeToLiveInSeconds) {
        logger.debug("starting revokation of token id: {} ", tokenId);

        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        // Store token in Redis cache with a specific TTL (expiration time)
        valueOperations.set(tokenId, tokenType, Duration.ofSeconds(timeToLiveInSeconds));

        logger.debug("token is revoked");
    }

    public Boolean isTokenRevoked(String tokenId) {
        logger.debug("checking revokation status of token {} ", tokenId);

        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Optional<String> tokenOpt = Optional.ofNullable((String) valueOperations.get(tokenId));
        Boolean status = tokenOpt.isPresent();

        logger.debug("Revokation status is {}", status);
        
        return status;
    }

}
