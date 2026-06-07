package com.rajeswari.urlshortener.scheduler;

import com.rajeswari.urlshortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClickFlushScheduler {

    private final StringRedisTemplate redisTemplate;
    private final UrlRepository urlRepository;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void flushClickCounts() {

        log.info("Starting click flush job...");

        ScanOptions options = ScanOptions.scanOptions()
                .match("click:*")
                .count(100)
                .build();

        Cursor<String> cursor = redisTemplate.scan(options);

        while (cursor.hasNext()) {

            String key = cursor.next();

            try {

                String shortCode = key.split(":")[1];
                String value = redisTemplate.opsForValue().get(key);

                if (value == null) continue;

                long count = Long.parseLong(value);

                if (count > 0) {
                    urlRepository.incrementClickCountBy(shortCode, count);
                    redisTemplate.delete(key);

                    log.info("Flushed {} clicks for shortCode={}", count, shortCode);
                }

            } catch (Exception e) {
                log.error("Error processing key {}", key, e);
            }
        }

        log.info("Click flush job completed.");
    }
}