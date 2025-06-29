package com.example.demo.services;

import static org.mockito.Mockito.*;

import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
public class RedisCacheServiceTest {

    @InjectMocks
    private RedisCacheService redisCacheService;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    private final String tokenId = "abc123";
    private final String tokenType = "access_token";

    @Test
    public void shouldRevokeToken() {
        long ttl = 3600L;
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        redisCacheService.revokeToken(tokenId, tokenType, ttl);

        verify(valueOperations).set(tokenId, tokenType, Duration.ofSeconds(ttl));
    }

    @Test
    public void shouldReturnTrueWhenTokenIsRevoked() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(tokenId)).thenReturn(tokenType);

        Boolean result = redisCacheService.isTokenRevoked(tokenId);

        Assertions.assertTrue(result);
    }

    @Test
    public void shouldReturnFalseWhenTokenIsNotRevoked() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(tokenId)).thenReturn(null);

        Boolean result = redisCacheService.isTokenRevoked(tokenId);

        Assertions.assertFalse(result);
    }
}
