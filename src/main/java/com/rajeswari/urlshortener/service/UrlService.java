package com.rajeswari.urlshortener.service;

import com.rajeswari.urlshortener.model.Url;
import com.rajeswari.urlshortener.repository.UrlRepository;
import com.rajeswari.urlshortener.util.Base62Encoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;

    public String createShortUrl(String originalUrl) {

        // Step 1: Save initial row (to generate ID)
        Url url = Url.builder()
                .originalUrl(originalUrl)
                .createdAt(LocalDateTime.now())
                .clickCount(0L)
                .active(true)
                .build();

        Url saved = urlRepository.save(url);

        // Step 2: Encode DB ID
        String shortCode = Base62Encoder.encode(saved.getId());

        // Step 3: Update entity
        saved.setShortCode(shortCode);
        urlRepository.save(saved);

        return shortCode;
    }

    @Transactional
    public Url getOriginalUrl(String shortCode) {

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Short URL not found"));

        urlRepository.incrementClickCount(shortCode);

        return url;
}
}