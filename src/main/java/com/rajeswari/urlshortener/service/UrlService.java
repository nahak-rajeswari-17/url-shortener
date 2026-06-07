package com.rajeswari.urlshortener.service;

import com.rajeswari.urlshortener.dto.CreateShortUrlRequest;
import com.rajeswari.urlshortener.exception.UrlNotFoundException;
import com.rajeswari.urlshortener.model.Url;
import com.rajeswari.urlshortener.repository.UrlRepository;
import com.rajeswari.urlshortener.util.Base62Encoder;
import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor

public class UrlService {

    private final UrlRepository urlRepository;
    private final StringRedisTemplate redisTemplate;

    public String createShortUrl(CreateShortUrlRequest request) {

        // Redis handles Concurrency better than DB, so we use Redis to generate unique
        // IDs with Persistence ON
        Long id = redisTemplate.opsForValue().increment("global:url:id");
        String shortCode = Base62Encoder.encode(id);

        Url url = Url.builder()
                .id(id)
                .originalUrl(request.getOriginalUrl())
                .shortCode(shortCode)
                .createdAt(LocalDateTime.now())
                .clickCount(0L)
                .active(true)
                .expiresAt(request.getExpiresAt())
                .build();

        urlRepository.save(url);

        return shortCode;
    }

    @Transactional
    public Url getOriginalUrl(String shortCode) {

        log.info("Incoming redirect request for shortCode={}", shortCode);

        // Try fetching original URL from Redis first
        String redisKey = "url:" + shortCode;
        String cachedUrl = redisTemplate.opsForValue().get(redisKey);

        Url url;
        if (cachedUrl != null) {
            log.info("Cache hit for shortCode={}", shortCode);
            // Optional: deserialize if storing JSON instead of plain string
            url = urlRepository.findByShortCode(shortCode)
                    .orElseThrow(() -> new UrlNotFoundException("Short URL not found"));
        } else {
            log.info("Cache miss for shortCode={}, fetching from DB", shortCode);
            url = urlRepository.findByShortCode(shortCode)
                    .orElseThrow(() -> new UrlNotFoundException("Short URL not found"));

            // Store in Redis with initial TTL
            long ttl = url.getExpiresAt() != null
                    ? Duration.between(LocalDateTime.now(), url.getExpiresAt()).getSeconds()
                    : 24 * 3600; // 24 hours for default

            redisTemplate.opsForValue().set(redisKey, url.getOriginalUrl(), Duration.ofSeconds(ttl));
            log.info("Stored shortCode={} in Redis with TTL={} seconds", shortCode, ttl);
        }

        // Validate active and expiry
        if (!url.isActive()) {
            throw new UrlNotFoundException("Short URL is inactive");
        }

        if (url.getExpiresAt() != null && url.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Short URL has expired");
        }

        // Increment click count in Redis
        String clickKey = "click:" + shortCode;
        Long newClickValue = redisTemplate.opsForValue().increment(clickKey);

        // Refresh TTL on every access (sliding TTL)
        redisTemplate.expire(redisKey, Duration.ofDays(1)); // sliding TTL of 1 day
        redisTemplate.expire(clickKey, Duration.ofDays(1));

        log.info("URL access logged for shortCode={}, Redis click count={}", shortCode, newClickValue);

        return url;
    }
}