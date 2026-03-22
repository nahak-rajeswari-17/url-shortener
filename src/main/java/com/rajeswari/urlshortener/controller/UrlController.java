package com.rajeswari.urlshortener.controller;

import com.rajeswari.urlshortener.dto.CreateShortUrlRequest;
import com.rajeswari.urlshortener.dto.CreateShortUrlResponse;
import com.rajeswari.urlshortener.service.UrlService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/urls")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @PostMapping
    public ResponseEntity<CreateShortUrlResponse> createShortUrl(
            @Valid@RequestBody CreateShortUrlRequest request) {

        String shortCode = urlService.createShortUrl(request);

        String shortUrl = "http://localhost:8080/u/" + shortCode;

        CreateShortUrlResponse response =
            CreateShortUrlResponse.builder()
                    .originalUrl(request.getOriginalUrl())
                    .shortUrl(shortUrl)
                    .shortCode(shortCode)
                    .build();

        return ResponseEntity.ok(response);
    }
}